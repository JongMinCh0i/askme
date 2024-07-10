✅ 개발자 Q&A 커뮤니티 서비스 입니다. 

---

## **🛠️ 기술 스택**

---

| 분류 | 기술명 |
| --- | --- |
| BackEnd | Java, Spring (Boot, Security, JPA), Junit, Mockito, Redis, MySql |
| DevOps | AWS(EC2, RDS, S3, GithubAction) |
| Tools | IntelliJ, Gradle|


## 🗂️ 패키지 구조

---

- com.aksme
    - domain
        - api
            - controller
                - request
            - service
                - reqeust
                - response
        - common
            - config
            - constant
            - error
            - http
            - interceptor
            - jwt
            - resolver
            - uitl
            - ResultResponse
        - dao

## 🔥 기술적 개선 및 고려

---

**게시글 조회 수 로직 개선[[적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/api/service/article/ViewCountSyncScheduler.java#L30)/[설정코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/config/RedisConfig.java#L19)]**

- 매번 게시글 조회수를 업데이트할 경우 DB 커넥션의 부하가 지속적으로 증가하게 되어 Redis를 도입하여 이를 처리함.
- 이후 실제 DB와의 데이터 정합성을 위해 @Scheduled을 사용하여 1분마다 지속적으로 DB에 update 쿼리가 수행됨.

**예외 정의 및 예외 메시지 커스텀 처리[[적용코드,](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/error/ErrorCode.java#L7) [적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/exception/GlobalExceptionAdvice.java#L17)]**

- 인증 및 비즈니스 로직 오류를 효과적으로 관리하기 위해 맞춤형 예외 클래스를 생성함.
- `@RestControllerAdvice`를 활용하여 다양한 예외를 전역적으로 처리하고, 상세한 오류 정보를 로깅하며 API 클라이언트에게 의미 있는 피드백을 제공함.
- 특정 오류 코드를 HTTP 상태와 매핑하여 일관된 에러 응답을 구현함으로써 애플리케이션의 안정성과 디버깅 효율성을 향상시킴.
- 글로벌 예외 처리 로직을 통해 내부 서버 오류, 비즈니스 예외, 유효성 검증 오류, 인증 예외 등을 구체적으로 핸들링함.

**RestTemplate 추가 설정 및 에러 핸들링 구현 [[설정코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/config/RestTemplateConfig.java#L12)]**

- RestTemplate의 연결 시간 초과(TTL)를 1초, 읽기 시간 초과(TTL)를 5초로 설정하여 안정적인 HTTP 클라이언트 설정을 구성
- `RestTemplateBuilder`를 사용하여 1초의 연결 시간 초과와 5초의 읽기 시간 초과를 설정하여 네트워크 요청의 안정성을 향상
- RestTemplate을 활용한 HTTP 요청 시 발생할 수 있는 예외 상황을 적절히 처리하여 클라이언트의 신뢰성을 높임.

**인터페이스 계층을 활용한 파일 업로드 서비스 DI 및 확장성 개선 [[적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/api/service/file/FileStorageService.java#L9),[적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/api/service/file/S3FileStorageService.java#L30),[적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/api/service/file/FileService.java#L23)]**

- `FileStorageService` 인터페이스 계층을 추가하여 구체적인 구체화를 추상화 하여 DIP, OCP 원칙을 준수함
- `FileService`는 인터페이스를 통해 파일 저장 서비스를 의존하게 하여, 다양한 파일 저장소 구현체를 쉽게 교체하고 확장 가능하도록 설계

**Parameter Resolver를 통한 컨트롤러 메서드 간소화 및 토큰 처리 개선 [[적용 코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/resolver/accountInfo/AccountArgumentResolver.java#L19), [적용코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/resolver/tokenInfo/TokenArgumentResolver.java#L13)]**

- `HandlerMethodArgumentResolver`를 사용하여 컨트롤러 메서드에서 반복되는 토큰 검증 및 파싱 로직을 분리하여 코드의 간결성과 재사용성을 높임.
- `AccountArgumentResolver`를 통해 `@AccountInfo` , `@TokenParser` 애노테이션이 붙은 메서드 파라미터에 대해 토큰에서 사용자 정보를 추출하여 `AccountDto` , `TokenDto`  객체를 자동으로 주입함.
- Authorization 헤더 유효성 검사를 `AuthorizationHeaderUtils`를 사용하여 일관되게 처리함으로써 코드의 중복을 제거하고 유지보수성을 향상시킴.

**공통 응답 클래스 개선[[적용 코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/common/ResultResponse.java#L3)]**

- 제네릭을 활용하여 다양한 타입의 응답 데이터를 처리할 수 있는 `ResultResponse` 클래스를 도입함.
- `success` 메서드를 통해 성공적인 응답을 쉽게 생성할 수 있으며, `fail` 메서드를 통해 실패 메시지를 포함한 응답을 간편하게 생성할 수 있도록 구현함.
- 응답의 일관성과 가독성을 높여 클라이언트와의 통신에서 발생할 수 있는 혼란을 줄이고, 코드의 재사용성을 향상시킴.

**다중 이미지 업로드 및 비동기 처리로 응답 속도 개선 [[적용 코드](https://github.com/JongMinCh0i/askme/blob/38eb57a3dc4d7a84328af1f3722217143287b4d1/src/main/java/com/example/askme/api/service/file/FileService.java#L46)]**

- `@Async`를 사용하여 비동기 처리로 이미지 업로드를 수행함으로써 서버의 응답 속도를 개선하고, 사용자가 빠른 피드백을 받을 수 있도록 함.
- `AsyncConfig`를 통해 비동기 처리를 위한 스레드 풀 설정을 구성하여 효율적인 리소스 관리를 가능하게 함.
- 이미지 파일의 확장자 유효성 검사를 통해 잘못된 파일 업로드를 사전에 방지하고, AWS S3에 안전하게 업로드되도록 함.
