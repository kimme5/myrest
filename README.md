1. RESTful API
   - (REpresentational State Transfer)ful 
   - 정보들을 주고받는데 있어서 개발자들 사이에 널리 쓰이는 일종의 형식을 의미함
   - 우체국에서 택배를 보내려면 송장을 작성해야 하듯이, 어떤 기술이나 제품이 아닌 형식을 의미함
   - HTTP(Hyper Text Transfer Protocol) : 주고받는 규약
   - http를 통해 Web 상의 특정 자원에 대한 transaction 처리하는 경우,
     여기에 접근하기 위한 url 또는 uri에 대상 자원정보를 추가하고 이를 CRUD 형태로 전달
     - 도서관리 프로그램인 경우, http://127.0.0.1:8080/book/1...
     - 회원관리 프로그램인 경우, http://127.0.0.1:8080/member/1...
   - 이들 자원에 대한 접근 및 처리를 CRUD에 맞춰 posting함
     - C(Create) : @Postmapping
     - R(Read)   : @Getmapping
     - U(Update) : @Putmapping
     - D(Delete) : @DeleteMapping
   - 과거의 복잡한 soap을 대체함
   - RESTful하게 만든 API는 요청을 보내는 주소만으로도 대략 이게 뭘 하는 요청인지 파악이 가능함
     - https://(도메인)/classes/2 --> 2번 인덱스에 해당하는 반
     - https://(도메인)/classes/4/students/15 --> 4번 인덱스에 해당하는 반에서 15번 인덱스에 해당하는 학생
     - https://(도메인)/classes/2/students?page=2&count=10 --> 2번 인덱스에 해당하는 학급의 학생들 중 10명씩 묶여 2번째 인덱스 페이지에 해당하는 대상

3. API(Application Programming Interface)
   - 소프트웨어가 다른 소프트웨어로 부터 지정된 형식으로 요청, 명령을 받을 수 있는 수단
   - Window Programing API
   - Google API

4. MethodSecurityConfig.java
   - 'ROLE_ADMIN' 권한을 갖는 사용자만 특정 게시글을 삭제하도록 권한에 따라 삭제 버튼을 보여주도록 처리함
     - form.html 삭제 버튼 내에 "sec:authorize="hasRole('ROLE_ADMIN')"를 설정함
   - 하지만, 삭제 버튼이 보이지 않는 'ROLE_ADMIN' 권한이 없는 사용자라도 POSTMAN을 통해 특정 게시글을 강제로 삭제할 수 있는 보안 취약점이 존재함
     - POSTMAN 에서 "delete : localhost:8100/api/users/8" 라고 입력하면 8번 게시글이 삭제됨
   - 이를 막기 위해 설정 클래스 파일을 생성하고 Controller에서 security annotation을 추가함
     - MethodSecurityConfig.java 생성
     - BoardApiController 클래스에서 deleteBoard() 메소드에 '@Secured("ROLE_ADMIN")' 을 설정함
   - 이렇게 하면 POSTMAN에서 권한이 없는 사용자가 삭제를 시도하려는 경우에도 먼저 로그인을 진행하도록 유도하게 됨

5. build.gradle 에 querydsl 라이브러리 추가하는 방법
   - https://data-make.tistory.com/728 참조하여 build.gradle 에 라이브러리 정보 추가
   - 프로젝트를 rebuild 하거나 재실행하면, 
     - myrest/build/generated/queyrdsl[main] 소스 루트/com/godcoder/myrest/model/ 
     - 해당 디렉토리 안에 QBoard, QRole, QUser 3개의 QClass 들이 생성되어 있음을 확인함 