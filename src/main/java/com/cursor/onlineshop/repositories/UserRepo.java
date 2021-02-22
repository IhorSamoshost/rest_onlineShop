package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
