package com.cos.security1.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    private String provider; //"google"
    private String providerId; //sub

    @CreationTimestamp
    private Timestamp createDate;

}
