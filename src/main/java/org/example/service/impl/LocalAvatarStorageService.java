package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
import org.example.config.FileStorageProperties;
import org.example.service.AvatarStorageService;
import org.example.util.FileUploadUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class LocalAvatarStorageService implements AvatarStorageService {

    private static final String PUBLIC_PREFIX = "/api/files/uploads/";

    private final Path uploadPath;

    public LocalAvatarStorageService(FileStorageProperties fileStorageProperties) {
        this.uploadPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException exception) {
            throw new IllegalStateException("Avatar katalogini yaratib bo'lmadi.", exception);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Avatar fayli bo'sh bo'lmasligi kerak.");
        }

        if (!FileUploadUtils.isImage(file.getContentType())) {
            throw new BusinessException("Faqat rasm fayllarini yuklash mumkin.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = FileUploadUtils.extractExtension(originalFilename);

        String storedFilename = UUID.randomUUID() + extension;
        Path targetLocation = uploadPath.resolve(storedFilename).normalize();

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Avatar faylini saqlab bo'lmadi.", exception);
        }

        return PUBLIC_PREFIX + storedFilename;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path filePath = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException ignored) {
            // no-op
        }

        throw new ResourceNotFoundException("Avatar fayli topilmadi.");
    }

    @Override
    public void deleteIfExists(String avatarUrl) {
        if (!StringUtils.hasText(avatarUrl) || !avatarUrl.startsWith(PUBLIC_PREFIX)) {
            return;
        }

        String filename = avatarUrl.substring(PUBLIC_PREFIX.length());
        try {
            Files.deleteIfExists(uploadPath.resolve(filename).normalize());
        } catch (IOException ignored) {
            // no-op
        }
    }
}
