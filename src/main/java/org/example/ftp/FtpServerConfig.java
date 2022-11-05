package org.example.ftp;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.example.ftp.handler.FtpLetHandler;
import org.example.ftp.users.CustomUserManager;
import org.example.ftp.users.FtpUsers;
import org.example.ftp.users.JpaFtpUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class FtpServerConfig {


    private final static int PORT = 10000;
    private final static String PASSIVE_PORT = "10001-10100";


    /**
     * FTP Server
     * {@link org.apache.ftpserver.impl.DefaultFtpServer}
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServer(UserManager userManager, FtpLetHandler ftpLetHandler) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.addListener("foobar", this.listener());

        serverFactory.setConnectionConfig(this.connectionConfig());

        serverFactory.setUserManager( userManager );

        //FTP Let set
        serverFactory.setFtplets(Map.of("exampleFtpLet", ftpLetHandler));

        return serverFactory.createServer();
    }

    /**
     * ConnectionConfig 설정: 최대 로그인 수와 같은 설정을 담당
     * {@link org.apache.ftpserver.impl.DefaultConnectionConfig}
     */
    @Bean
    public ConnectionConfig connectionConfig() {
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setMaxLogins(10);
        connectionConfigFactory.setMaxAnonymousLogins(0);
        return connectionConfigFactory.createConnectionConfig();
    }


    /**
     * DataConnectionConfiguration : 실제 데이터(파일)과 송수신 담당 설정
     * {@link org.apache.ftpserver.impl.DefaultDataConnectionConfiguration}
     */
    @Bean
    public DataConnectionConfiguration dataConnectionConfiguration() {
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        // 패시브 포트 설정
        dataConnectionConfigurationFactory.setPassivePorts(PASSIVE_PORT);
        // SSL 설정
        dataConnectionConfigurationFactory.setSslConfiguration(null);
        // FTP 서버 활성화 여부
        dataConnectionConfigurationFactory.setActiveEnabled(true);
        // FTP 접근시 IP 체크 여부
        dataConnectionConfigurationFactory.setActiveIpCheck(false);
        // 유휴 시간 설정
        dataConnectionConfigurationFactory.setIdleTime(3000);

        return dataConnectionConfigurationFactory.createDataConnectionConfiguration();
    }




    /**
     * Listener : 클라이 언트 연결 및 처리 담당
     * {@link org.apache.ftpserver.listener.nio.AbstractListener}
     */
    @Bean
    public Listener listener() {
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setIdleTimeout(3000);
        // FTP 서버 접속 Port Set
        listenerFactory.setPort(PORT);
        listenerFactory.setSslConfiguration(null);
        // Config Set
        listenerFactory.setDataConnectionConfiguration( this.dataConnectionConfiguration() );
//        listenerFactory.setMax
        return listenerFactory.createListener();
    }

    /**
     * FTP User Manager
     * Tutorial 용도임.  Custom implements Class {@link CustomUserManager}
     * {@link org.apache.ftpserver.usermanager.impl.AbstractUserManager}
     * {@link org.apache.ftpserver.usermanager.impl.DbUserManager}
     * {@link org.apache.ftpserver.usermanager.impl.PropertiesUserManager}
     */
    @Bean
    public UserManager ftpUserManager(JpaFtpUserRepository jpaFtpUserRepository) throws FtpException {
//        UserManager tutorialUseManager = new PropertiesUserManager(new ClearTextPasswordEncryptor(), new File(""), "admin" );
//        // FTP User interface 의 기본 구현체
//        BaseUser baseUser = new BaseUser();
//        baseUser.setName("admin");
//        baseUser.setPassword("1234");
//        baseUser.setHomeDirectory("/");
//        baseUser.setEnabled(true);
//        //UserManager를 통해 테스트용 User 추가
//        tutorialUseManager.save(baseUser);
//        return tutorialUseManager;

        UserManager userManager = new CustomUserManager(jpaFtpUserRepository);
        FtpUsers admin = new FtpUsers("admin","1234","/");
        FtpUsers testUser = new FtpUsers("test","1234","/test");
        userManager.save(admin);
        userManager.save(testUser);
        return userManager;
    }


    /**
     * FTP User Manager Factory
     * {@link org.apache.ftpserver.usermanager.DbUserManagerFactory}
     * {@link org.apache.ftpserver.usermanager.PropertiesUserManagerFactory}
     */
    @Bean
    public UserManagerFactory userManagerFactory(JpaFtpUserRepository jpaFtpUserRepository) throws FtpException {
//        UserManager userManager = this.ftpUserManager();
        UserManager userManager = new CustomUserManager(jpaFtpUserRepository);
        return new UserManagerFactory(){
            @Override
            public UserManager createUserManager() {
                return userManager;
            }
        };

    }





}
