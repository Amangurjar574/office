package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_Detail")
public class User_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(name="name" , nullable = false)
    private String name;

    @Column(name="email" ,unique = true, nullable = false)
    private String email;


    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false)
    private String role ;

    @Column(name = "status", nullable = false)
    private  String status="deactivate";

    @Column(name = "isDeleted", nullable = false)
    private  boolean isDeleted=false;

    @Column(name = "created_date")
    private LocalDateTime created_date;

    @Column(name = "Update_date")
    private LocalDateTime Update_date;

    @Column(name = "jwtforsetpassword")
    private String jwtforsetpassword="null";

}
