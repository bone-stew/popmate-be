package com.bonestew.popmate.popupstore.config.service;

import com.bonestew.popmate.popupstore.config.FolderType;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Optional<String> upload(MultipartFile multipartFile, FolderType folderType);
}