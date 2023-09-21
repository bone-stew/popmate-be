package com.bonestew.popmate.popupstore.config.service;

import com.bonestew.popmate.popupstore.config.FolderType;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Optional<String> upload(MultipartFile multipartFile, FolderType folderType);

    List<String> uploadFiles(List<MultipartFile> multipartFile, FolderType folderType);

    String uploadInputStream(InputStream inputStream, String directory);
}
