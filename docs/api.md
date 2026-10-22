# API 목록

Base URL: `http://localhost:8080` (NCP 배포 후에는 서버 주소로 교체)
Swagger UI: `/swagger-ui.html` · OpenAPI JSON: `/v3/api-docs`

## 공통 규칙

- **로그인(데모)**: 로그인이 필요한 API는 요청 헤더 `X-Student-Id: <학생 ID>`로 학생을 구분합니다. 학생 ID는 `GET /api/students` 에서 고릅니다.
- **학번 비노출**: 응답에는 학번이 없고 익명 닉네임만 나옵니다.
- **에러 형식**: 모든 에러는 `{"status": 404, "code": "COURSE_NOT_FOUND", "message": "..."}` 형태입니다. 프론트는 `code`로 분기합니다.

## 엔드포인트

| 구분 | 메서드 | 경로 | 로그인 | 설명 | 주요 파라미터 | 성공 응답 | 에러 코드 |
|---|---|---|---|---|---|---|---|
| 강의 | GET | `/api/courses` | 불필요 | 강의(개설 분반) 목록·검색. 학수번호 → 분반 순 정렬 | `keyword`(과목명·학수번호·교수명), `department`, `courseType`, `weekday`(1~7), `page`(기본 0), `size`(기본 20, 1~100) | 200 `PageResponse<CourseResponse>` | 400 `INVALID_PARAMETER` |
| 강의 | GET | `/api/courses/filters` | 불필요 | 검색 드롭다운용 학과·이수구분·요일 선택지 | 없음 | 200 `CourseFiltersResponse` | - |
| 강의 | GET | `/api/courses/{offeringId}` | 불필요 | 분반 1개의 상세·수업 시간·교환 현황 | `offeringId`(경로) | 200 `CourseResponse` | 404 `COURSE_NOT_FOUND`, 400 `INVALID_PARAMETER` |
| 학생 | GET | `/api/students` | 불필요 | 데모 로그인용 학생 목록(학번 제외) | 없음 | 200 `StudentSummaryResponse[]` | - |
| 학생 | GET | `/api/students/me` | 필요 | 내 정보 + 현재 수강 학점 | 헤더 `X-Student-Id` | 200 `MyProfileResponse` | 401 `MISSING_STUDENT_ID`, 400 `INVALID_STUDENT_ID`, 404 `STUDENT_NOT_FOUND` |
| 수강 | GET | `/api/enrollments/me` | 필요 | 내 수강 목록(시간표). 각 항목에 수업 시간 포함 | 헤더 `X-Student-Id` | 200 `MyEnrollmentsResponse` | 401 `MISSING_STUDENT_ID`, 400 `INVALID_STUDENT_ID`, 404 `STUDENT_NOT_FOUND` |
| 인프라 | GET | `/actuator/health` | 불필요 | 서버 상태 확인(로드밸런서 헬스체크용) | 없음 | 200 `{"status":"UP"}` | - |

## 에러 코드

| code | HTTP | 의미 |
|---|---|---|
| `MISSING_STUDENT_ID` | 401 | `X-Student-Id` 헤더 없음 |
| `INVALID_STUDENT_ID` | 400 | 헤더가 1 이상의 숫자가 아님 |
| `STUDENT_NOT_FOUND` | 404 | 존재하지 않는 학생 |
| `COURSE_NOT_FOUND` | 404 | 존재하지 않는 분반 |
| `INVALID_PARAMETER` | 400 | 쿼리·경로 파라미터 값 오류 |
| `NOT_FOUND` | 404 | 없는 주소 |
| `METHOD_NOT_ALLOWED` | 405 | 지원하지 않는 HTTP 메서드 |
| `INTERNAL_ERROR` | 500 | 서버 내부 오류 |

## 응답 필드

### CourseResponse (강의 1건)

| 필드 | 타입 | 설명 | 예 |
|---|---|---|---|
| `offeringId` | number | 분반 ID. 수강·교환·찜 모두 이 값을 사용 | 9 |
| `courseId` | number | 과목 ID | 5 |
| `courseCode` | string | 학수번호 | CSE3050 |
| `courseName` | string | 과목명 | 프로그래밍언어론 |
| `sectionNo` | string | 분반 | 01 |
| `professorName` | string | 교수명 | 서교수 |
| `department` | string | 개설 학과 | 컴퓨터공학과 |
| `courseType` / `courseTypeLabel` | string | 이수 구분 코드 / 한글 | MAJOR_ELECTIVE / 전공선택 |
| `credits` | number | 학점 | 3 |
| `capacity` / `enrolledCount` / `full` | number / number / boolean | 정원 / 현재 수강 인원 / 마감 여부 | 30 / 2 / false |
| `classroom` | string | 강의실 | 공학관 501 |
| `schedules[]` | object[] | `weekday`, `weekdayLabel`, `startTime`, `endTime`("HH:mm") | 2, 화, 15:00, 16:15 |
| `scheduleText` | string | 수업 시간 한 줄 표시 | 화목 15:00-16:15 |
| `exchange` | object | `wantCount`(교환 희망자 수), `offerCount`(보유자 수). 인원수만 공개 | 3, 1 |

### PageResponse

`content[]`, `page`, `size`, `totalElements`, `totalPages`, `hasNext`

### MyEnrollmentsResponse

| 필드 | 설명 |
|---|---|
| `studentId`, `nickname` | 학생 ID, 익명 닉네임 |
| `totalCredits`, `maxCredits` | 현재 수강 학점 합계 / 신청 가능 최대 학점 |
| `count` | 수강 과목 수 |
| `enrollments[]` | `enrollmentId`(교환 요청 때 내놓을 강의로 사용), `enrolledAt`, `course`(CourseResponse) |

### MyProfileResponse

`studentId`, `nickname`, `department`, `yearLevel`, `maxCredits`, `currentCredits`, `blockedUntil`(없으면 null)

### StudentSummaryResponse

`studentId`(헤더에 넣는 값), `nickname`, `department`, `yearLevel`

## 호출 예시

```bash
# 강의 검색
curl "http://localhost:8080/api/courses?keyword=프로그래밍&weekday=2"

# 내 수강 목록 (1번 학생)
curl -H "X-Student-Id: 1" http://localhost:8080/api/enrollments/me

# 헤더 없이 호출하면 401
curl -i http://localhost:8080/api/enrollments/me
```

## 앞으로 추가할 API (미구현)

찜(wishlist), 교환 요청 등록·취소, 대기열 조회, 매칭 세션 동의·거절·깨우기, 패널티 조회. 스키마(`V1__init_schema.sql`)에는 테이블이 이미 있습니다.
