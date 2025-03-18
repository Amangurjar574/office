package com.example.demo2.repo;

import com.example.demo2.entity.import_user_entitys.ConversionDataOfUser;
import com.example.demo2.entity.import_user_entitys.RawDataOfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionDataOfUserRepo extends JpaRepository<ConversionDataOfUser,String> {
    ConversionDataOfUser findByFileName(String fileName);
}
