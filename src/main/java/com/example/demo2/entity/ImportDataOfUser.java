package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Import_Data_Of_User")
public class ImportDataOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ImportDataId;

    @ManyToOne
    @JoinColumn(name = "userDetail", referencedColumnName = "email")
    private User_Detail userDetail;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "s3FilePath")
    private String s3FilePath;

    @Column(name = "s3Bucket")
    private String s3Bucket;

    @Column(name = "s3Url")
    private String s3Url;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "UpdateDate")
    private LocalDateTime updatedDate;

    @Column(name = "dataCount")
    private long dataCount;

    @Column(name = "status")
    private String status;

    @Column(name = "isDeleted")
    private boolean isDeleted=false;

}

