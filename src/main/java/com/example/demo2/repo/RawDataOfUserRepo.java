package com.example.demo2.repo;

import com.example.demo2.entity.import_user_entitys.RawDataOfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataOfUserRepo extends JpaRepository<RawDataOfUser,String> {
     RawDataOfUser findByFileName(String fileName);
}
