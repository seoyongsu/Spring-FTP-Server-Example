package org.example.ftp;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.PropertiesUserManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FtpServerConfig {


    private final static int PORT = 10000;
    private final static String PASSIVE_PORT = "10001-10100";

    /**
     * FTP Server
     * {@link org.apache.ftpserver.impl.DefaultFtpServer}
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServer() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.addListener("foobar", this.listener());

        serverFactory.setUserManager(this.ftpUserManager());

        return serverFactory.createServer();
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
        // FTP 서버 접속 Port Set
        listenerFactory.setPort(PORT);
        // Config Set
        listenerFactory.setDataConnectionConfiguration( this.dataConnectionConfiguration() );

        return listenerFactory.createListener();
    }

    /**
     * FTP User Manager
     * {@link org.apache.ftpserver.usermanager.impl.AbstractUserManager}
     * {@link org.apache.ftpserver.usermanager.impl.DbUserManager}
     * {@link org.apache.ftpserver.usermanager.impl.PropertiesUserManager}
     */
    @Bean
    public UserManager ftpUserManager() throws FtpException {

        UserManager userManager = new PropertiesUserManager(new ClearTextPasswordEncryptor(), new File(""), "admin" );
        // FTP User interface 의 기본 구현체
        BaseUser baseUser = new BaseUser();
        baseUser.setName("admin");
        baseUser.setPassword("1234");
        baseUser.setHomeDirectory("/");
        baseUser.setEnabled(true);
        
        //UserManager를 통해 테스트용 User 추가
        userManager.save(baseUser);
        
        return userManager;
    }



}
