package org.example.ftp;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.impl.DefaultDataConnectionConfiguration;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.DefaultFtpServerContext;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpServerConfig {

    private final static int PORT = 10000;
    private final static String PASSIVE_PORT = "10001-10100";

    /**
     * FTP Server
     * {@link org.apache.ftpserver.impl.DefaultFtpServer}
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.addListener("foobar", this.listener());
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



}
