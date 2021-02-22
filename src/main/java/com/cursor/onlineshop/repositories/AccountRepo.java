package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
}
