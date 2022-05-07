package com.cos.security1.repository;

import com.cos.security1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//
public interface UserRepository extends JpaRepository<User, Long> {
    //findBy 규칙 -> Username 문법
    //select * from user where username = ? 실행
    public User findByUsername(String username);
}
