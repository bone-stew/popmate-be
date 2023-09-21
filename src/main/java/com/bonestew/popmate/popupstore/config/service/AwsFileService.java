package com.bonestew.popmate.popupstore.config.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.exception.ImageUploadFailedException;
import com.bonestew.popmate.popupstore.config.exception.AwsS3Exception;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<String> uploadFiles(List<MultipartFile> multipartFileList, FolderType folderType) {
        List<String> urlList = new ArrayList<>();
        try {
            for(MultipartFile file: multipartFileList){
                Optional<String> imgUrl = upload(file, folderType);
                urlList.add(imgUrl.get());
            }
        } catch (SdkClientException e) {
            throw new ImageUploadFailedException();
        }
        return urlList;
    }

    @Override
    public String uploadInputStream(InputStream inputStream, String directory) {
        try (InputStream resource = inputStream) {
            String fileName = generateFileName(directory);
            amazonS3Client.putObject(bucketName, fileName, resource, null);
            return getResourceUrl(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AwsS3Exception();
        }
    }

    private String generateFileName(String directory) {
        return String.format("%s/%s.png", directory, getRandomUUID());
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getResourceUrl(String savedFileName) {
        return amazonS3Client.getResourceUrl(bucketName, savedFileName);
    }
}
