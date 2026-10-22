# 수강신청 교환 마켓 · 백엔드 (course_change_be)

돈 거래 없이 수강 중인 강의를 서로 맞교환하는 서비스의 백엔드입니다. 가상 학사 시스템(학생·강의·수강) 위에 교환 기능이 붙는 구조입니다.

| 항목 | 내용 |
|---|---|
| 언어·프레임워크 | Java 17, Spring Boot 3.5 (Web, Data JPA, Validation, Actuator) |
| DB | MySQL 8.0 (스키마는 Flyway가 자동 생성) |
| API 문서 | springdoc Swagger UI |
| 테스트 | JUnit 5 + Testcontainers(실제 MySQL 컨테이너) |
| CI | GitHub Actions (테스트 + jar 실행 스모크 테스트) |

## 실행 방법

### 준비물

- JDK 17 이상
- Docker (로컬 MySQL과 테스트용 컨테이너에 사용)

### 1. DB 켜기

```bash
docker compose up -d        # MySQL 8.0 (localhost:3306, DB/계정/비번 모두 course_change)
docker compose down         # 중지
docker compose down -v      # 데이터까지 지우고 처음부터
```

### 2. 서버 켜기

```bash
./gradlew bootRun
```

서버가 뜨면 Flyway가 `V1__init_schema.sql`(테이블 11개)과 `V2__seed_sample_data.sql`(가상 학생 10명, 강의 21개 등)을 자동으로 적용합니다.

- Swagger UI: http://localhost:8080/swagger-ui.html
- 헬스체크: http://localhost:8080/actuator/health

Swagger UI에서 로그인이 필요한 API는 우측 상단 **Authorize**에 학생 ID(예: `1`)를 넣으면 `X-Student-Id` 헤더가 자동으로 붙습니다.

### 3. 테스트

```bash
./gradlew test                          # Docker가 켜져 있어야 함 (Testcontainers)
bash scripts/smoke-test.sh              # 서버를 띄운 뒤 주요 API를 curl로 확인
```

### 환경변수 (배포 시)

| 이름 | 기본값 | 설명 |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/course_change?...` | DB 주소 (NCP Cloud DB for MySQL 주소로 교체) |
| `DB_USERNAME` / `DB_PASSWORD` | `course_change` | DB 계정 |
| `CORS_ALLOWED_ORIGINS` | localhost:3000, 5173 | 프론트 주소 (쉼표로 여러 개) |
| `PORT` | `8080` | 서버 포트 |

배포용 jar: `./gradlew bootJar` → `build/libs/app.jar`

## 문서

- [API 목록](docs/api.md): 엔드포인트 표, 에러 코드, 응답 필드
- [ERD](docs/erd.png) (원본 [erd.mmd](docs/erd.mmd)): 테이블 11개 관계도
- [23일 회의 안건](docs/day23-agenda.md)
- 스키마 원본: `src/main/resources/db/migration/V1__init_schema.sql`

## 구현 현황

| 기능 | 상태 |
|---|---|
| 서버 뼈대, 공통 에러 형식, Swagger | 완료 |
| 강의 목록·검색·상세·필터 | 완료 |
| 데모 로그인(`X-Student-Id`), 내 정보, 내 수강 목록 | 완료 |
| 찜(하트·장바구니) | 예정 |
| 교환 요청 등록·취소 (희망 분반 우선순위) | 예정 |
| 대기열·우선순위 점수 | 예정 |
| 매칭 세션(동의·거절·타이머·깨우기), 원자적 교환 | 예정 |
| 패널티·이용 제한 | 예정 |
| 알림 (NCP Simple & Easy Notification Service) | 예정 |

## 폴더 구조

```
src/main/java/com/ncp03/coursechange/
├─ common/      에러 처리, CORS·Swagger 설정, X-Student-Id 처리
├─ domain/      JPA 엔티티·리포지토리 (11개 테이블)
├─ course/      강의 조회 API
├─ student/     학생(데모 로그인) API
└─ enrollment/  내 수강 목록 API
src/main/resources/db/migration/   Flyway 스키마·샘플 데이터
```

DB를 바꿀 때는 이미 적용된 `V1`, `V2` 파일을 고치지 말고 `V3__설명.sql`처럼 새 파일을 추가합니다. (적용된 파일을 수정하면 Flyway 체크섬 오류가 납니다.)

## 23일 팀원 할 일 메모

### 백엔드

- [ ] 찜 API: 추가·삭제·내 찜 목록 (학점 상한으로 막지 않음)
- [ ] 교환 요청 API: 내 수강 내역 중 내놓을 강의 + 희망 분반 우선순위 등록·취소
  - 희망 분반은 분반까지 정확히 지정, 내가 가진 분반 자체는 희망 불가
  - 같은 수강 내역은 진행 중 요청 1개에만 묶임 (DB 제약 있음)
- [ ] 대기열 조회 API: 우선순위 점수 정렬, 순위와 인원수만 노출
- [ ] 매칭 로직: 서로의 희망이 맞는 1:1 쌍만 매칭, 학점 상한·최종 시간표 겹침 검사
- [ ] 세션 동의·거절·깨우기(최대 3회), 5분 타이머 만료 처리, 감점 반영
- [ ] 교환 확정 트랜잭션: 수강 두 건을 한 번에 맞바꾸고, 실패 시 전체 롤백

### 프론트

- [ ] 경희대 수강신청 화면을 참고한 강의 검색 화면 (`GET /api/courses`)
- [ ] 데모 로그인 화면 (`GET /api/students`에서 고른 ID를 `X-Student-Id`로 전송)
- [ ] 내 시간표 화면 (`GET /api/enrollments/me`)

### 인프라·테스트

- [ ] NCP: Cloud DB for MySQL 생성, Auto Scaling·Load Balancer 구성
- [ ] GitHub Actions 배포 자동화 (CD)
- [ ] 부하테스트(k6) 시나리오: 강의 조회, 교환 요청 동시 접수

### 회의에서 정할 것

[23일 회의 안건](docs/day23-agenda.md) 참고.
