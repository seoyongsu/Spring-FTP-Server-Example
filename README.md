# Spring-FTP-Server-Example


https://mina.apache.org/

https://gitbox.apache.org/repos/asf/mina-ftpserver.git


## FTP 중요 인터페이스

FtpServer : FTP 서버 실행, 종료 관련 interface

ConnectionConfig : 커넥션 config 관련 interface

DataConnectionConfiguration : 데이터 전송 관련 Config interface

Listener : client 접속 관련 config interface

UserManager : FTP User 관련 interface

UserManagerFactory : UserManager 생성 관련 interface

## Application 실행 방법
1. gradle build
2. docker-compose up  또는 Spring boot 실행
3. [fileZilla-Client](https://filezilla-project.org/) 통해서 접속 테스트 (admin/1234, test/1234)
   - 접속포트는 10000
   - admin/1234 (관리자 예제 계정)
   - test/1234  (일반사용자 예제 계정)
