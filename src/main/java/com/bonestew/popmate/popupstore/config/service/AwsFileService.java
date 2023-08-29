package com.bonestew.popmate.popupstore.config.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bonestew.popmate.popupstore.config.FolderType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsFileService implements FileService {

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    @Override
    public Optional<String> upload(MultipartFile multipartFile, FolderType folderType) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            InputStream inputStream = multipartFile.getInputStream();
            String fileKey = folderType.getFolderName() + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileKey, inputStream, objectMetadata));
            return Optional.of(amazonS3Client.getUrl(bucketName, fileKey).toString());
        } catch (SdkClientException | IOException e) {
            return Optional.empty();
        }
    }
}
