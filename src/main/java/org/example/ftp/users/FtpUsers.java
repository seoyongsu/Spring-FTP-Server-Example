package org.example.ftp.users;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginRequest;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * FTP USER interface 구현 Class {@link User}
 * <pre>
 *     Spring Security의 UserDetails와 흡사함.
 * </pre>
 */
@Entity
@Table(name = "ftp_users")
public class FtpUsers implements User {

    @Id
    private String userId;
    private String password;
    private String directory;


    protected FtpUsers(){}
    public FtpUsers (User user){
        this.userId = user.getName();
        this.password = user.getPassword();
        this.directory = user.getHomeDirectory();
    }
    public FtpUsers(String userId, String password, String directory) {
        this.userId = userId;
        this.password = password;
        this.directory = directory;
    }

    @Override
    public String getName() {
        return this.userId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public List<? extends Authority> getAuthorities() {
        return Arrays.asList(new ConcurrentLoginPermission(1,1));
    }

    @Override
    public List<? extends Authority> getAuthorities(Class<? extends Authority> aClass) {
        return Arrays.asList(new ConcurrentLoginPermission(1,1));
    }

    /**
     * FTP User의 인증 요청 방식
     * 사용자가 동시에 여러 세션에 로그인할 수 있는 권한 설정
     * {@link org.apache.ftpserver.ftplet.AuthorizationRequest}
     * 사용자에 대한 전송 속도 제한을 설정
     * {@link org.apache.ftpserver.usermanager.impl.TransferRateRequest}
     * 사용자가 파일 쓰기에 대한 권한 설정
     * {@link org.apache.ftpserver.usermanager.impl.WriteRequest}
     */
    @Override
    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
        return new ConcurrentLoginRequest(1, 1);
    }

    @Override
    public int getMaxIdleTime() {
        return 0;
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public String getHomeDirectory() {
        return this.directory;
    }
}
