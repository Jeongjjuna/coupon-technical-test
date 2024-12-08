## ⛄개발환경
- macOS
- JDK 21
- Language : Java
- FrameWork : Spring Boot
- Build : Gradle
- DB Framework : JPA
- DataBase : 인메모리 H2 DataBase

---

## ⛄테스트
[macOS 기준]
- 테스트 실행 & 결과 레포트 확인
```
./gradlew clean test && open build/reports/tests/test/index.html
```

## ⛄빌드 / 실행방법
[macOS 기준]
- 프로젝트 빌드하기 & 실행하기
```
./gradlew clean build && java -jar build/libs/coupon-0.0.1-SNAPSHOT.jar
```

---

## 🎄프로젝트 구조
main 패키지 java.org.coupon
- common
  - 공통 응답, 예외 핸들러, 설정 파일
- coupon : 레이어드 아키텍처 구조로 개발
  - application : 서비스 역할
  - domain : 핵심 비즈니스 도메인
  - infrastructure : 레포지토리등의 외부 인프라 역할
  - presentation : 컨트롤러 역할
- issue : 레이어드 아키텍처 구조로 개발
  - application : 서비스 역할
  - domain : 핵심 비즈니스 도메인
  - infrastructure : 레포지토리등의 외부 인프라 역할
  - presentation : 컨트롤러 역할

---

## ⛄ API 명세서

![](https://img.shields.io/static/v1?label=&message=GET&color=red)
![](https://img.shields.io/static/v1?label=&message=POST&color=blue)

### 쿠폰 등록 API

> ![](https://img.shields.io/static/v1?label=&message=POST&color=blue) <br>
> localhost:8080/**v1/coupons**

<details>
<summary>상세보기</summary>

#### Parameters

##### Body

|        name        |  type  | description |       required        |
|:------------------:|:------:|:-----------:|:---------------------:|
|    coupon_type     | string |    쿠폰 타입    | **Required, NonNull** |
|    description     | string |    쿠폰 설명    | **Required, NonNull** |
| total_coupon_count |  int   |  등록할 쿠폰 개수  | **Required, NonNull** |

#### Response 예시

200 Ok : 성공적으로 로그인 된 경우

  ```
    {
      "result_code": 200,
      "data": {
        "id": 1,
        "coupon_type": "BASIC",
        "description": "일반 쿠폰",
        "total_coupon_count": 100,
        "issued_coupon_count": 0,
        "created_at": "2024-12-08T16:27:33.593666",
        "updated_at": "2024-12-08T16:27:33.593666",
        "suspended_at": null
      }
    }
  ```

400 BadRequest : 유효성 검사에 실패한 경우

  ```
    {
      "result_code": 400,
      "timestamp": "2024-12-08T16:29:10.199889",
      "error_messages": [
        "couponType : 쿠폰타입은 null 일 수 없습니다.",
        "description : 쿠폰 설명은 null 일 수 없습니다."
      ]
    }
  ```

</details>
<br>


---

### 쿠폰 코드 발급 요청 API

> ![](https://img.shields.io/static/v1?label=&message=POST&color=blue) <br>
> localhost:8080/**v1/issues**


<details markdown="1">
<summary>상세보기</summary>

#### Parameters

##### Body

|   name    | type | description |       required        |
|:---------:|:----:|:-----------:|:---------------------:|
| member_id | int  |  발급할 유저 id  | **Required, NonNull** |
| coupon_id | int  |  발급할 쿠폰 id  | **Required, NonNull** |

#### Response 예시

200 Ok : 성공적으로 쿠폰 코드 발급한 경우

  ```
    {
      "result_code": 200,
      "data": {
        "coupon_code": "1733644181693VWR"
      }
    }
  ```

409 NotFound : 정지된 쿠폰에 대해 발급요청한 경우

  ```
    {
      "result_code": 409,
      "timestamp": "2024-12-08T17:04:53.974216",
      "error_messages": [
        "사용이 정지된 쿠폰유형 입니다."
      ]
    }
  ```

</details>
<br>


---

### 쿠폰 사용 API

> ![](https://img.shields.io/static/v1?label=&message=POST&color=blue) <br>
> localhost:8080/**v1/issues/redeem**


<details>
<summary>상세보기</summary>

#### Parameters

##### Body

|    name     |  type  | description |       required        |
|:-----------:|:------:|:-----------:|:---------------------:|
|  member_id  | string |  쿠폰 사용자 id  | **Required, NonNull** |
| coupon_code | string |  사용할 쿠폰 코드  | **Required, NonNull** |

#### Response

200 Ok : 성공적으로 쿠폰 사용한 경우

  ```
    {
      "result_code": 200,
      "data": null
    }
  ```

404 NotFound : 발행한 쿠폰코드를 찾을 수 없는 경우

  ```
    {
      "result_code": 404,
      "timestamp": "2024-12-08T16:52:34.003294",
      "error_messages": [
        "쿠폰 발행을 찾을 수 없습니다."
      ]
    }
  ```

</details>
<br>

---

### 쿠폰 일괄 정지 API

> ![](https://img.shields.io/static/v1?label=&message=POST&color=blue) <br>
> localhost:8080/**v1/coupons/{id}/suspend**


<details>
<summary>상세보기</summary>

#### Parameters

##### Path

| name | type |  description  |       required        |
|:----:|:----:|:-------------:|:---------------------:|
|  id  | int  | 일괄 정지시킬 쿠폰 id | **Required, NonNull** |


#### Response

200 Ok : 성공적으로 일괄 정지된 경우

  ```
    {
    "result_code": 200,
    "data": null
    }
  ```

404 NotFound : 정지할 쿠폰을 찾을 수 없는 경우

  ```
    {
      "result_code": 404,
      "timestamp": "2024-12-08T17:02:56.865725",
      "error_messages": [
        "쿠폰을 찾을 수 없습니다."
      ]
    }
  ```

</details>
<br>


---

## 🎄요구사항

- [x] 쿠폰을 생성 할 수 있다.(create)
    - [x] 쿠폰은 타입당 n개 생성 할 수 있다.
    - [x] 쿠폰은 최소 1개 이상 생성해야 한다.
    - [x] 지원되지 않는 타입의 쿠폰은 생성할 수 없다.
- [x] 쿠폰코드를 발행 할 수 있다.(issue)
    - [x] 쿠폰 코드는 숫자 + 알파벳 조합이다.
    - [x] 쿠폰 코드는 16자리 이다.
- [x] 쿠폰코드를 통해 쿠폰을 사용할 수 있다.(redeem)
    - [x] 1개의 쿠폰코드는 여러번 사용될 수 없다.
- [x] 특정 타입의 쿠폰을 일괄 정지할 수 있다.(suspend)
    - [x] 정지된 쿠폰에 대한 쿠폰 코드를 발급할 수 없다.
    - [x] 정지된 쿠폰에 대한 쿠폰 코드를 사용할 수 없다.

---

## 🎄동시성 고려

- [x] 1개 쿠폰 코드에 대해 여러번 사용되면 안된다.
    - 쿠폰발급은 트래픽이 몰릴 수 있지만, 실제 쿠폰 사용은 트래픽 몰림까지는 아닐거라고 예상
    - synchronized 활용하기로 결정(서버 분산 고려는 하지 않았음)
- [x] 쿠폰 코드 발급 시 정해진 수량 이상으로 발급되면 안된다.
    - 쿠폰 조회시 비관적 쓰기락을 활용해서 DB를 잠금.
      - DB 락보다는 애플리케이션 관점에서 lock 매커니즘을 사용하는 것이 더 좋을 수 있다.
      - 레이어드 아키텍처에서 DataBase 에 접근하는 것보다는, Controller 에서 lock 으로 필터링하는게 좋을 수 있다.

---

## 🎄누적되는 쿠폰 데이터 조회 성능 고려
- coupon_id 를 통해 특정 coupon_id 에 해당하는 coupon_issue 데이터를 조회할 일이 많을 것이라고 예상됨.
- coupon_id 에 인덱스를 적용.
```sql
SELECT *
FROM coupon_issue
WHERE
    coupon_id = 1
  AND is_used = TRUE;
```