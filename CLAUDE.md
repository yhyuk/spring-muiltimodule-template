# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.3.5 멀티모듈 프로젝트 템플릿. Java 21, Gradle 8.5, MySQL 8.x 기반.

## Build & Run Commands

```bash
# 전체 빌드
./gradlew clean build

# api-sample 모듈 실행 (port 8080)
./gradlew :api-sample:bootRun

# 테스트 실행
./gradlew test                        # 전체 테스트
./gradlew :api-sample:test            # 특정 모듈 테스트
./gradlew test --tests "*.SampleServiceTest"  # 단일 테스트 클래스

# Swagger UI: http://localhost:8080/swagger-ui.html
```

## Module Architecture

3개 모듈이 계층적으로 의존: `api-sample -> common -> domain`

- **domain** (라이브러리, `bootJar=false`): JPA Entity, `BaseEntity`(createdAt/updatedAt auditing). 순수 도메인 모델만 포함. 패키지: `com.template.multimodule.domain`
- **common** (라이브러리, `bootJar=false`, `java-library` plugin): 공통 인프라 — `ApiResponse<T>` 응답 래퍼, `BusinessException`/`ErrorCode` 예외 체계, `GlobalExceptionHandler`, JPA/Web 설정. 패키지: `com.template.multimodule.common`
- **api-sample** (실행 가능, `bootJar=true`): Spring Boot Application. Controller-Service-Repository 레이어. Swagger(springdoc) 포함. 패키지: `com.template.multimodule.api`

## Key Patterns

- **API 응답**: 모든 컨트롤러는 `ApiResponse<T>`로 래핑하여 반환. `ApiResponse.success(data)` / `ApiResponse.error(message)` 사용.
- **예외 처리**: `ErrorCode` enum에 HTTP 상태/코드/메시지 정의 -> `BusinessException(ErrorCode)` throw -> `GlobalExceptionHandler`가 처리.
- **Entity Auditing**: 모든 엔티티는 `BaseEntity` 상속 (`@CreatedDate`, `@LastModifiedDate`). `JpaConfig`에서 `@EnableJpaAuditing` 활성화.
- **트랜잭션**: Service 클래스에 `@Transactional(readOnly = true)` 기본 적용, 쓰기 메서드에만 `@Transactional` 오버라이드.
- **의존성 관리**: 루트 `build.gradle`의 `ext` 블록에서 버전 중앙 관리. `subprojects`에서 공통 의존성(Lombok, Validation, Test) 적용.

## Adding a New API Module

1. `settings.gradle`에 모듈 추가
2. 모듈 `build.gradle`에서 `org.springframework.boot` 플러그인 적용, `implementation project(':common')`, `implementation project(':domain')` 의존
3. `bootJar.enabled = true`, `jar.enabled = false` 설정
4. `@SpringBootApplication` 메인 클래스 생성

## Adding a New Domain Entity

1. `domain` 모듈에 `BaseEntity` 상속 엔티티 생성
2. `common`의 `ErrorCode`에 도메인별 에러 코드 추가
3. API 모듈에 Repository(JPA), Service, Controller, Request/Response DTO 구현
