# 💻 PreOnboarding Backend Course(Java) : 회원가입 / 로그인 기능 구현

---
## 🔍 요구 사항

- Junit를 이용한 테스트 코드 작성법 이해
  - JUnit를 이용한 JWT Unit 테스트 코드 작성해보기
- Spring Security를 이용한 Filter에 대한 이해
  - Filter란 무엇인가?(with Interceptor, AOP)
  - Spring Security란?
- JWT와 구체적인 알고리즘의 이해
  - JWT란 무엇인가요?
- 토큰 발행과 유효성 확인
  - Access / Refresh Token 발행과 검증에 관한 테스트 시나리오 작성하기
- PR 날려보기
  - 좋은 커밋 메세지 작성하기
- Refactoring
  - 피드백 받아서 코드 개선하기
- API 접근과 검증
  - Swagger UI로 접속 가능하게 하기
- 배포
  - AWS EC2에 배포해보기
  

---

## 🚀 기능 요구 사항

### 회원가입 / 로그인 기능을 구현한다.

- 회원가입 - /signup
  - Request Message
    ```json
    {
      "username": "JIN HO",
      "password": "12341234",
      "nickname": "Mentos"
    }
    ``` 
  - Response Message
    ```json
      {
        "username": "JIN HO",
        "nickname": "Mentos",
        "authorities": [
              {
                      "authorityName": "ROLE_USER"
          }
        ]
      }
    ``` 

- 로그인 - /sign
  - Request Message
    ```json
    {
      "username": "JIN HO",
      "password": "12341234",
    }
    ``` 
  - Response Message
    ```json
      {
        "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL",
      }
    ```
    
---

### 📍 피드백
- `Spring Security + JWT`를 이용해 구현해보기
  - JWT 라이브러리는 잘 사용했지만 공유해 준 래퍼런스를 보고, JWT가 발급되는 과정에 대해 좀 더 공부하고 다시 구현해봤으면 좋겠다.
- JwtUtil에서 로그가 노출되는 부분에 대해 어떻게 생각하는지
  - `validateToken` 메소드에서 로그가 상세하게 노출되는 부분에 대해 어떻게 생각하는지 궁금하다.
  ```java
   public boolean validateToken(String token) {
    try {
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    return true;
    } catch (SecurityException | MalformedJwtException e) {
    log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
    log.error("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
    log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
    log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
    }
  ```
- Entity에 사용한 어노테이션에 대한 설명
  - 생성자 어노테이션의 Access Level
    - `Access Level`을 왜 `PRIVATE`, `PROTECTED`로 설정했는지 설명할 수 있었으면 좋겠다.
    ```java
      @NoArgsConstructor(access = AccessLevel.PROTECTED)
      @AllArgsConstructor(access = AccessLevel.PRIVATE)
      ```
  - `@CreatedDate`를 사용한 이유
    - `updatable = false`를 설정한 이유에 대해 설명할 수 있었으면 좋겠다.
    - `@EntityListeners(AuditingEntityListener.class)`의 역할에 대해 설명할 수 있었으면 좋겠다.
     ```java
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime issuedAt;
    ```


### 📍 개선 사항
-[x] `Spring Security + JWT`를 이용해 구현해보기 (진행중)
-[x] `JwtUtil`에서 로그가 노출되는 부분
  - 문제점
    - JWT token 검증 중 발생한 예외를 로그에 상세하게 기록하는 것은 개발 및 운영 시 문제를 진단하는 데 유용할 수 있지만, 로그에 민감한 정보를 노출하게 되면 보안적인 문제를 초래할 수 있다.
    - 예를 들면, `"Invalid JWT signature"`와 같은 메시지는 공격자에게 서명 검증에 실패했다는 것을 알려주며, 이를 통해 서명 알고리즘에 대한 정보를 유추할 수 있게 된다.
  - 해결방안
    - 로그 레벨을 환경에 맞게 설정하여 개발 환경에서는 디버깅에 필요한 세부 정보를 기록하되, 운영 환경에서는 최소한의 정보를 기록하도록 한다.
    - 로그 메시지에서 구체적인 예외 정보나 민감한 데이터를 제거하고, 일반적인 오류 메시지를 남기는 것이 좋다.
  ```java
     public boolean validateToken(String token) {
      try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
      } catch (SecurityException | MalformedJwtException e) {
      log.error("JWT validation failed.");
      log.debug("Detailed error: Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
      } catch (ExpiredJwtException e) {
      log.error("JWT validation failed.");
      log.debug("Detailed error: Expired JWT token, 만료된 JWT token 입니다.");
      } catch (UnsupportedJwtException e) {
      log.error("JWT validation failed.");
      log.debug("Detailed error: Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
      } catch (IllegalArgumentException e) {
      log.error("JWT validation failed.");
      log.debug("Detailed error: JWT claims is empty, 잘못된 JWT 토큰 입니다.");
      }
      return false;
      }
    ```


-[x] Entity에 사용한 어노테이션에 대한 설명
  -[x] 생성자 어노테이션에서 `AccessLevel.PRIVATE, PROTECTED`를 사용한 이유
    - 생성자 어노테이션에서 AccessLevel을 설정한 이유는 외부에서의 무분별한 객체 생성을 막기 위해서이다.
    - JPA는 기본 생성자가 없으면 엔티티를 생성할 수 없기 때문에 JPA가 객체를 생성할 수 있도록 하기 위해 기본 생성자는 PROTECTED로 설정하였다.
    - 모든 필드를 초기화하는 생성자는 필요시 빌터 패턴을 사용해서 객체를 생성하기 위해 PRIVATE으로 설정하였다.
  -[x] `@CreateDate`, `@Column(updatable = false)`를 사용한 이유
    - Entity가 생성되어 저장될 때 시간이 자동 저장되며, 생성일의 업데이트를 막기위해 사용했다.
  -[x] `@EntityListeners(AuditingEntityListener.class)`를 사용한 이유
    - JPA에서는 `Audit`이라는 기능을 제공하고 있다. Audit은 `감시하다`라는 뜻으로 Spring Data JPA에서 시간에 대해 자동으로 값을 넣어주는 기능이다.
      이를 사용하여 엔티티가 생성되고, 변경되는 시점을 감지하여 생성 시각, 수정 시각, 생성한 사람, 수정한 사람을 기록할 수 있다.
    - 해당 클래스에 Auditing 기능을 포함하기 위해 사용했다.


### 📍 Reference
- https://velog.io/@limsubin/Spring-Security-JWT-%EC%9D%84-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90
