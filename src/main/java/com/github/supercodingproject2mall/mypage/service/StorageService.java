package com.github.supercodingproject2mall.mypage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class StorageService {
    private AmazonS3 s3Client;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build();
    }
    public String uploadFile(MultipartFile file) throws IOException {
        String bucketName = bucket;
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
            return s3Client.getUrl(bucketName, fileName).toString();
        }
    }
}
