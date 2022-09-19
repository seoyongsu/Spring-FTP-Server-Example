package org.example.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.DefaultFtpServerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpServerConfig {

    /**
     * FTP Server
     * {@link org.apache.ftpserver.impl.DefaultFtpServer}
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServerFactory() {
        return new DefaultFtpServer(new DefaultFtpServerContext());
    }

}
