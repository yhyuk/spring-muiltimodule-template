# Spring Boot Multi-Module Template

Spring Boot 3.x 기반의 멀티모듈 프로젝트 템플릿입니다.

## 프로젝트 구조

```
spring-multimodule-template/
├── domain/                 # 도메인 엔티티 모듈
├── common/                 # 공통 유틸리티 모듈
├── api-sample/             # 샘플 API 모듈
├── build.gradle            # 루트 빌드 설정
└── settings.gradle         # 모듈 설정
```

## 모듈 설명

### 1. domain 모듈
- **역할**: JPA Entity, VO, Domain Model 관리
- **특징**:
  - 순수 도메인 로직만 포함
  - `bootJar=false, jar=true` (라이브러리로 사용)
- **주요 클래스**:
  - `BaseEntity`: 공통 Entity 속성 (createdAt, updatedAt)
  - `SampleEntity`: 샘플 엔티티

### 2. common 모듈
- **역할**: 공통 유틸리티, 설정, 예외 처리
- **특징**:
  - `java-library` plugin 적용
  - domain 모듈 의존
  - `bootJar=false, jar=true` (라이브러리로 사용)
- **주요 구성**:
  - `dto/ApiResponse`: 공통 API 응답 포맷
  - `exception/`: 공통 예외 처리 (BusinessException, ErrorCode, GlobalExceptionHandler)
  - `config/`: 공통 설정 (JpaConfig, WebConfig)
  - `util/`: 유틸리티 클래스 (DateUtils)

### 3. api-sample 모듈
- **역할**: 실행 가능한 Spring Boot Application
- **특징**:
  - `bootJar=true` (실행 가능한 jar)
  - common, domain 모듈 의존
- **주요 구성**:
  - Controller, Service, Repository 레이어
  - Swagger UI 포함
  - CRUD 샘플 API 구현

## 기술 스택

- Java 21
- Spring Boot 3.3.5
- Spring Data JPA
- MySQL 8.x
- Lombok
- Springdoc OpenAPI (Swagger)
- Gradle

## 시작하기

### 1. 사전 요구사항

- JDK 21 이상
- MySQL 8.x
- Gradle 8.x (또는 Gradle Wrapper 사용)

### 2. 데이터베이스 설정

MySQL에 데이터베이스 생성:

```sql
CREATE DATABASE sample_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 설정 파일 수정

`api-sample/src/main/resources/application.yml` 파일에서 데이터베이스 접속 정보를 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sample_db?useSSL=false&serverTimezone=Asia/Seoul
    username: root
    password: your_password
```

### 4. 빌드 및 실행

```bash
# 전체 빌드
./gradlew clean build

# api-sample 모듈 실행
./gradlew :api-sample:bootRun

# 또는 jar 파일 실행
java -jar api-sample/build/libs/api-sample.jar
```