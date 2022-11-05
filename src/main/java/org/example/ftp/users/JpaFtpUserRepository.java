package org.example.ftp.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFtpUserRepository extends JpaRepository<FtpUsers, String> {

    boolean existsByUserId(String userId);
}
