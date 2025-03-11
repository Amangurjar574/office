package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    private String role ;

    @Column
    private  String status="deactivate";

    @Column
    private  boolean isDeleted=false;

     @Column
    private String jwtforsetpassword="null";

}
