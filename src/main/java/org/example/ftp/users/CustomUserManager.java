package org.example.ftp.users;


import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import javax.persistence.EntityNotFoundException;


/**
 * FTP User Manager
 * {@link org.apache.ftpserver.usermanager.impl.AbstractUserManager}
 * {@link org.apache.ftpserver.usermanager.impl.DbUserManager}
 * {@link org.apache.ftpserver.usermanager.impl.PropertiesUserManager}
 *
 */
public class CustomUserManager implements UserManager {

    private final static String ROOT_PATH = System.getProperty("user.dir")+"/ftpserver";

    private final JpaFtpUserRepository jpaFtpUserRepository;

    public CustomUserManager(JpaFtpUserRepository jpaFtpUserRepository) {
        this.jpaFtpUserRepository = jpaFtpUserRepository;
    }

    @Override
    public User getUserByName(String s) {
        return jpaFtpUserRepository.findById(s).orElseThrow(()->new EntityNotFoundException(s + " : 아이디가 존재하지 않음"));
    }

    @Override
    public String[] getAllUserNames(){
        return jpaFtpUserRepository.findAll()
                .stream()
                .map(FtpUsers::getName)
                .toArray(String[]::new);
    }

    @Override
    public void delete(String s)  {

    }

    @Override
    public void save(User user)  {
        FtpUsers ftpUsers = new FtpUsers(user.getName(), user.getPassword(), ROOT_PATH+user.getHomeDirectory());
        jpaFtpUserRepository.save(ftpUsers);
    }

    @Override
    public boolean doesExist(String s){
        return jpaFtpUserRepository.existsByUserId(s);
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        
        if(authentication instanceof UsernamePasswordAuthentication auth){
            //사용자 처리
            return this.getUserByName(auth.getUsername());
        } else if(authentication instanceof AnonymousAuthentication auth){
            //익명 사용자
            // TODO...
            return new BaseUser();
        } else {
            throw new AuthenticationFailedException("지원 되지 않음");
        }
    }

    @Override
    public String getAdminName() throws FtpException {
        return "admin";
    }

    @Override
    public boolean isAdmin(String s) throws FtpException {
        return s.equals("admin");
    }

}
