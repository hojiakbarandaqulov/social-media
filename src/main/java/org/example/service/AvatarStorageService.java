package org.example.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {

    String store(MultipartFile file);

    Resource loadAsResource(String filename);

    void deleteIfExists(String avatarUrl);
}
