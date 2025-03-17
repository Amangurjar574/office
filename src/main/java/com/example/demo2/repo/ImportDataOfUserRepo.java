package com.example.demo2.repo;

import com.example.demo2.entity.ImportDataOfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportDataOfUserRepo  extends JpaRepository<ImportDataOfUser,String> {
     ImportDataOfUser findByFileName(String fileName);
}
