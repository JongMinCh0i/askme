package com.example.askme.dao.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByRefreshToken(String refreshToken);

    Optional<Account> findByNickname(String nickname);
}
