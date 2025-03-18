package com.example.demo2.entity.import_user_entitys;

import com.example.demo2.entity.user_entitys.User_Detail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Conversion_Data_Of_User")
public class ConversionDataOfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ConversionDataId;//

    @ManyToOne
    @JoinColumn(name = "userDetail", referencedColumnName = "email")
    private User_Detail userDetail;//

    @Column(name = "segmentName")
    private String segmentName;//

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "conversionType")
    private String conversionType;

    @Column(name = "s3FilePath")
    private String s3FilePath;

    @Column(name = "s3Bucket")
    private String s3Bucket;//

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "UpdateDate")
    private LocalDateTime updatedDate;//

    @Column(name = "dataCount")
    private long dataCount;//

    @Column(name = "status")
    private String status;//

    @Column(name = "isDeleted")
    private boolean isDeleted=false;//

}
