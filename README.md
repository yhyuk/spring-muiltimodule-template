# Spring Boot Multi-Module Template

Spring Boot 3.3.5 + Java 21 기반의 멀티모듈 프로젝트 템플릿입니다.
새로운 API 서비스를 빠르게 시작할 수 있도록 공통 패턴(응답 래핑, 예외 처리, Entity Auditing)을 미리 구성해 두었습니다.

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| ORM | Spring Data JPA |
| Database | MySQL 8.x (테스트: H2) |
| Build | Gradle 8.x |
| API Docs | Springdoc OpenAPI (Swagger) |
| Container | Docker, Docker Compose |

## 모듈 아키텍처

```
api-sample  ──depends──>  common  ──depends──>  domain
(실행 가능)               (라이브러리)            (라이브러리)
```

### domain

순수 도메인 모델만 포함하는 라이브러리 모듈입니다.

- JPA Entity, `BaseEntity` (createdAt/updatedAt 자동 관리)
- `bootJar = false`, `java-library` 플러그인 적용 — 다른 모듈에서 의존성으로 사용
- 패키지: `com.template.multimodule.domain`

### common

공통 인프라를 제공하는 라이브러리 모듈입니다.

- `ApiResponse<T>` — 통일된 API 응답 포맷
- `BusinessException` / `ErrorCode` — 구조화된 예외 처리
- `GlobalExceptionHandler` — 전역 예외 핸들링
- JPA, Web, Jackson 공통 설정
- `bootJar = false`, `java-library` 플러그인 적용
- 패키지: `com.template.multimodule.common`

### api-sample

실행 가능한 Spring Boot Application 모듈입니다.

- Controller - Service - Repository 레이어 구조
- CRUD 샘플 API 구현
- Swagger UI, Actuator 포함
- `bootJar = true`
- 패키지: `com.template.multimodule.api`

## 시작하기

### 사전 요구사항

- JDK 21+
- MySQL 8.x (또는 Docker)

### 방법 1: Docker Compose (권장)

별도의 MySQL 설치 없이 바로 실행할 수 있습니다.

```bash
# 빌드 후 실행
./gradlew clean build
docker compose up -d
```

환경 변수로 설정을 변경할 수 있습니다.

```bash
DB_PASSWORD=my_password CORS_ALLOWED_ORIGINS=https://example.com docker compose up -d
```

### 방법 2: 로컬 실행

1. MySQL에 데이터베이스를 생성합니다.

```sql
CREATE DATABASE sample_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. `api-sample/src/main/resources/application-dev.yml`에서 DB 접속 정보를 확인합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sample_db?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: ${DB_PASSWORD:password}
```

3. 애플리케이션을 실행합니다.

```bash
./gradlew :api-sample:bootRun
```

4. Swagger UI에서 API를 확인합니다: http://localhost:8080/swagger-ui.html

## 빌드 및 테스트

```bash
# 전체 빌드
./gradlew clean build

# 전체 테스트
./gradlew test

# 특정 모듈 테스트
./gradlew :api-sample:test

# 단일 테스트 클래스
./gradlew test --tests "*.SampleServiceTest"
```

테스트 환경은 H2 인메모리 DB를 사용하므로 MySQL 없이도 실행됩니다.

## 환경 분리

Profile 기반으로 환경이 분리되어 있습니다.

| Profile | 용도 | DB | DDL 전략 |
|---------|------|-----|----------|
| `dev` (기본) | 로컬 개발 | MySQL (localhost) | `update` |
| `test` | 테스트 | H2 인메모리 | `create-drop` |
| `prod` | 운영 | MySQL (환경변수) | `validate` |

운영 환경에서는 모든 설정을 환경변수로 주입합니다 (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`).

## 핵심 패턴

### API 응답 포맷

모든 API는 `ApiResponse<T>`로 래핑하여 통일된 형식으로 응답합니다.

```json
{
  "success": true,
  "data": { "id": 1, "name": "Sample", "description": "..." },
  "timestamp": "2025-01-01T00:00:00"
}
```

```java
// 컨트롤러에서 사용
return ResponseEntity.ok(ApiResponse.success(data));
return ResponseEntity.ok(ApiResponse.success(data, "생성 완료"));
```

### 예외 처리

`ErrorCode` enum에 정의한 에러 코드를 `BusinessException`으로 throw하면, `GlobalExceptionHandler`가 자동으로 `ApiResponse` 형식의 에러 응답을 반환합니다.

```java
// ErrorCode에 에러 코드 정의
SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "Sample not found"),

// Service에서 throw
throw new BusinessException(ErrorCode.SAMPLE_NOT_FOUND);
```

```json
{
  "success": false,
  "message": "Sample not found",
  "errorCode": "S001",
  "timestamp": "2025-01-01T00:00:00"
}
```

### Entity Auditing

모든 엔티티는 `BaseEntity`를 상속하여 `createdAt`, `updatedAt`이 자동으로 관리됩니다.

```java
@Entity
public class SampleEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
```

### 트랜잭션 전략

Service 클래스에 `@Transactional(readOnly = true)`를 기본 적용하고, 쓰기 메서드에만 `@Transactional`을 오버라이드합니다.

```java
@Service
@Transactional(readOnly = true)
public class SampleService {

    public SampleResponse findById(Long id) { ... }  // readOnly

    @Transactional  // 쓰기
    public SampleResponse create(SampleRequest request) { ... }
}
```

## 새 API 모듈 추가하기

이 템플릿을 기반으로 새로운 API 모듈을 추가하는 방법입니다.

### 1. 모듈 등록

`settings.gradle`에 모듈을 추가합니다.

```groovy
include 'api-new-service'
```

### 2. build.gradle 작성

```groovy
plugins {
    id 'org.springframework.boot'
}

dependencies {
    implementation project(':common')
    implementation project(':domain')

    implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui:${springdocVersion}"
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    runtimeOnly 'com.mysql:mysql-connector-j'
    testRuntimeOnly 'com.h2database:h2'
}

bootJar {
    enabled = true
    archiveFileName = 'api-new-service.jar'
}

jar {
    enabled = false
}
```

### 3. Application 클래스 생성

```java
@SpringBootApplication(scanBasePackages = {
        "com.template.multimodule.api",
        "com.template.multimodule.common"
})
@EntityScan(basePackages = "com.template.multimodule.domain")
@EnableJpaRepositories(basePackages = "com.template.multimodule.api.repository")
public class ApiNewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiNewServiceApplication.class, args);
    }
}
```

### 4. 도메인 엔티티 추가 (필요 시)

1. `domain` 모듈에 `BaseEntity`를 상속한 엔티티 생성
2. `common`의 `ErrorCode`에 도메인 에러 코드 추가
3. API 모듈에 Repository, Service, Controller, DTO 구현

## 샘플 API

`api-sample` 모듈에 포함된 CRUD API 목록입니다.

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/samples` | 전체 목록 조회 |
| GET | `/api/samples/{id}` | 단건 조회 |
| POST | `/api/samples` | 생성 |
| PUT | `/api/samples/{id}` | 수정 |
| DELETE | `/api/samples/{id}` | 삭제 |

## 프로젝트 구조

```
spring-multimodule-template/
├── domain/                          # 도메인 모듈
│   └── src/main/java/.../domain/
│       ├── BaseEntity.java          # 공통 엔티티 (createdAt, updatedAt)
│       └── sample/
│           └── SampleEntity.java
├── common/                          # 공통 모듈
│   └── src/main/java/.../common/
│       ├── config/                  # JPA, Web 설정
│       ├── dto/
│       │   └── ApiResponse.java     # 통일 응답 포맷
│       ├── exception/               # 예외 처리 체계
│       │   ├── ErrorCode.java
│       │   ├── BusinessException.java
│       │   └── GlobalExceptionHandler.java
│       └── util/                    # 유틸리티
├── api-sample/                      # 샘플 API 모듈
│   ├── src/main/java/.../api/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
│   └── src/main/resources/
│       ├── application.yml          # 공통 설정
│       ├── application-dev.yml      # 개발 환경
│       ├── application-test.yml     # 테스트 환경 (H2)
│       └── application-prod.yml     # 운영 환경
├── Dockerfile                       # Docker 이미지 빌드
├── docker-compose.yml               # MySQL + App 실행
├── build.gradle                     # 루트 빌드 (의존성 중앙 관리)
└── settings.gradle                  # 모듈 등록
```
