package com.example.demo2.repo;

import com.example.demo2.entity.User_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<User_Detail,String> {
    User_Detail findByEmail(String email);
    User_Detail findByUserid(long userid);
    int deleteByUserid(long userid);

}
