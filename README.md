## Autocrypt 백엔드 과제 - 홍성진

***

**packaging** : `jar`

**Language** : `Java11`

**Build Tool** : `Gradle`

**Framework** : `Spring Boot 2.7.1`

**Database** : `MySQL`

**Document** : `Swagger 3.0`

***

### 🔧 Installation.

```shell
./gradlew init
./gradlew build
java -jar ./build/libs/board-info-api-0.0.1-SNAPSHOT.jar
```

### ✏️ Documents.

```shell
http://localhost:8080/swagger-ui.html
```

**초기 사용법**
- 회원가입및 로그인을 제외한 모든 요청에는 Jwt Token Authorization이 필요합니다.
1. **user-api-controller** 를 통해 회원가입을 진행합니다.

   <img width="80%" alt="스크린샷 2022-06-30 오후 4 57 26" src="https://user-images.githubusercontent.com/56334761/176624555-5395ccb1-0921-4012-a186-031caf1a5bf4.png">

2. **user-api-controller** 를 통해 로그인을 진행합니다. (로그인 성공 시, Jwt Token 발급)

   <img width="80%" alt="스크린샷 2022-06-30 오후 5 00 00" src="https://user-images.githubusercontent.com/56334761/176624904-80520c52-4a75-455f-b82c-b6382ae2660f.png">
   
   <img width="80%" alt="스크린샷 2022-06-30 오후 5 01 06" src="https://user-images.githubusercontent.com/56334761/176625233-d8959fb9-d9e7-47e0-a300-266b55bdd534.png">

3. **Global Authorization** 에 발급 받은 Jwt Token 을 입력합니다.
   
   <img width="80%" alt="스크린샷 2022-06-30 오후 5 02 41" src="https://user-images.githubusercontent.com/56334761/176625490-bb8a6550-e4e1-4a77-b074-8a24eefc8dec.png">
   
   <img width="80%" alt="스크린샷 2022-06-30 오후 5 03 07" src="https://user-images.githubusercontent.com/56334761/176625560-39317be8-32b4-4963-b7c9-ed3e8040c97d.png">

5. Jwt Token 인증 성공 시, 모든 요청에 대한 응답을 확인할 수 있게됩니다.

***

### 🚀 Description.

**ERD**

<img width="80%" alt="스크린샷 2022-06-30 오후 5 13 08" src="https://user-images.githubusercontent.com/56334761/176627399-40129cdb-a1c8-4a00-8410-4649d2be036f.png">

**Application Architecture**

<img width="80%" alt="스크린샷 2022-06-30 오후 5 23 02" src="https://user-images.githubusercontent.com/56334761/176629353-55ce2b34-fddf-477d-847c-0848e3baf1bb.png">

**Summary**

- **Business Logic**
  - User
    1. 회원가입 (Email 형식을 지켜야 하며 (중복 허용 X), Password는 2글자 이상으로 제한)
    2. 로그인 
  - Post (로그인된 회원만 가능)
    1. 게시판 등록
    2. 게시판 조회 (잠금되어 있는 게시물은 본인만 확인 가능)
    3. 게시물 삭제 (본인 확인 설정)
    4. 게시물 업데이트 (본인 확인 설정)
    5. 게시물 잠금 설정 및 해제 (본인 확인 설정)

- **Domain**
  1. 준영속 상태를 고려하여 `equals()` 와 `hashcode()`를 재정의 하였습니다.
  2. `@MappedSuperclass` , `@EnableJpaAuditing` 을 사용하여 엔티티 등록 시, 생성 시간과 update 시간을 저장하도록 설계하였습니다.

- **Core Strategy**
  1. `*ToOne` 관계는 `LAZY` 형태로 로딩함과 동시에, `fetch join`을 활용하여 성능을 최적화 시켰습니다.
  2. 회원가입, 로그인 이외의 모든 요청은 선제적으로 필터를 통해 `JWT Token`을 인증받도록 설정하였습니다.
  3. `Bean Validation` 을 통해 `Parameter Exception` 을 정의하였습니다.

- **Security**
    1. 대칭키 방식을 활용하여 `Jwt Server`를 통하여 `Token`을 발급 받습니다.
    2. 요청에 대한 `Custom Filter` 를 `UsernamePasswordAuthenticationFilter` 전에 등록하여,
       `Token`의 인증을 선제적으로 담당하도록 설계하였습니다.

***

### 🚀 Scenario.

1. **회원가입**
    - 정상적인 상황
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 19 16" src="https://user-images.githubusercontent.com/56334761/176641617-f708bbf5-4286-440e-aebc-b7bc640c8aa3.png">
   
    - Parameter Exception

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 23 15" src="https://user-images.githubusercontent.com/56334761/176642108-292dff03-ff2b-402f-a047-8f760165a6a7.png">
   
    - Duplicate Exception

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 23 37" src="https://user-images.githubusercontent.com/56334761/176642193-97d33b0b-47fe-4cf1-986a-f18e40407ce7.png">
   
2. **로그인**
    - 정상적인 상황 (토큰 발급)
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 25 28" src="https://user-images.githubusercontent.com/56334761/176642642-d8270706-6941-42d0-8056-5fb18663fbd6.png">
   
    - 비밀번호 오류
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 25 48" src="https://user-images.githubusercontent.com/56334761/176642783-d1c16b7d-03f4-4f82-87b1-f25485cd197c.png">
   
    - Parameter Exception

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 26 01" src="https://user-images.githubusercontent.com/56334761/176642925-dade40fc-5c07-40ab-b911-e890a8419ed8.png">

3. **로그인할 때 발급 받은 토큰을 활용하여 Bearer Authorization을 설정해야합니다. (밑의 모든 시나리오에 적용)**
    
   <img width="80%" alt="스크린샷 2022-06-30 오후 6 30 11" src="https://user-images.githubusercontent.com/56334761/176643524-d205cd08-26c5-49b0-bbd8-eebf57ccd7cb.png">

4. **게시판 등록**
    - 정상적인 상황
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 32 56" src="https://user-images.githubusercontent.com/56334761/176644059-d99cad06-590e-4623-a25f-ade5af2186d3.png">
    
    - Parameter Exception

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 33 12" src="https://user-images.githubusercontent.com/56334761/176644232-22ccb985-b895-452f-bcc1-da7bc144a8f6.png">
    
5. **게시판 조회**    
    - 정상적인 상황

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 35 41" src="https://user-images.githubusercontent.com/56334761/176644780-c7477d70-b57b-433c-9105-a8580b2373dd.png">

    - 잠금된 게시물을 본인이 아닌 User가 조회할 때 (다른 User의 Login Token을 다른 것을 집어 넣으면 됨)

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 38 49" src="https://user-images.githubusercontent.com/56334761/176645268-0ceda028-a590-474c-a313-66ec5270b442.png">
    
6. **게시판 업데이트 (게시판 등록때와 Parameter Exception은 같습니다.)**
    - 정상적인 상황
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 41 33" src="https://user-images.githubusercontent.com/56334761/176645991-ee953a37-cbf7-4b62-99f4-e83c771b13ce.png">
   
    - 본인이 아닐 때 (다른 User의 Login Token을 다른 것을 집어 넣으면 됨) 
   
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 41 16" src="https://user-images.githubusercontent.com/56334761/176645888-a3149ba1-4f36-48ba-9a3b-7ac92b8812d6.png">

7. **게시판 잠금설정 (Parameter 로 "DISABLED", "ENABLED"를 설정하면 됩니다.)**
   - 정상적인 상황
      
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 44 49" src="https://user-images.githubusercontent.com/56334761/176646702-844e6265-6e9d-423f-8dfb-f74455bc2a37.png">
     
      <img width="80%" alt="스크린샷 2022-06-30 오후 6 45 10" src="https://user-images.githubusercontent.com/56334761/176646814-967b8b4a-62bc-4cf8-8ae6-0a7485f0847b.png">
    
   - Parameter Exception 

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 47 34" src="https://user-images.githubusercontent.com/56334761/176647371-e692734f-07e9-4b8a-bc17-85c435cf3718.png">
     
   - 본인이 아닐 때 (다른 User의 Login Token을 다른 것을 집어 넣으면 됨) 

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 47 59" src="https://user-images.githubusercontent.com/56334761/176647412-07cb3e8c-6d56-4f56-b7c8-4e5852997d3e.png">
     
8. **게시판 삭제**
    - 정상적인 상황

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 50 07" src="https://user-images.githubusercontent.com/56334761/176647875-39ad9cd0-63f0-4147-980f-4ebce3a1244e.png">

    - 본인이 아닐 때 (다른 User의 Login Token을 다른 것을 집어 넣으면 됨) 

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 49 52" src="https://user-images.githubusercontent.com/56334761/176648024-da222f76-3e11-441e-9f6c-5bc3c9852a4e.png">
    
    - 삭제된 후, 조회시 예외 발생.

      <img width="80%" alt="스크린샷 2022-06-30 오후 6 52 09" src="https://user-images.githubusercontent.com/56334761/176648327-842c30db-6644-4e12-8830-607d4f01c5ea.png">

