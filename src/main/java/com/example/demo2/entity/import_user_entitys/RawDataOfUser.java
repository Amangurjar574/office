package com.example.demo2.entity.import_user_entitys;

import com.example.demo2.entity.user_entitys.User_Detail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Raw_Data_Of_User")
public class RawDataOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rawDataId;

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

