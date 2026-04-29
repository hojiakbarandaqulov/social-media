package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
import org.example.config.FileStorageProperties;
import org.example.dto.attach.AttachDTO;
import org.example.entity.Attach;
import org.example.repository.AttachRepository;
import org.example.service.AttachService;
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
public class AttachServiceImpl implements AttachService {

    private static final String PUBLIC_PREFIX = "/api/attach/";

    private final AttachRepository attachRepository;
    private final Path uploadPath;

    public AttachServiceImpl(
            AttachRepository attachRepository,
            FileStorageProperties fileStorageProperties
    ) {
        this.attachRepository = attachRepository;
        this.uploadPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException exception) {
            throw new IllegalStateException("Upload papkasini yaratib bo'lmadi.", exception);
        }
    }

    @Override
    public AttachDTO uploadFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new BusinessException("Fayl bo'sh bo'lmasligi kerak.");
            }

            if (!FileUploadUtils.isImage(file.getContentType())) {
                throw new BusinessException("Faqat rasm fayllarini yuklash mumkin.");
            }

            String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String extensionWithDot = FileUploadUtils.extractExtension(originalName);
            String extension = extensionWithDot.startsWith(".")
                    ? extensionWithDot.substring(1)
                    : extensionWithDot;
            String key = UUID.randomUUID().toString();
            String storedFileName = extension.isBlank() ? key : key + "." + extension;
            Path targetLocation = uploadPath.resolve(storedFileName).normalize();

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Attach entity = new Attach();
            entity.setId(key+"."+extension);
            entity.setOriginalName(originalName);
            entity.setExtension(extension);
            entity.setSize(file.getSize());
            entity.setPath(storedFileName);
            attachRepository.save(entity);

            return toDTO(entity);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Faylni upload qilib bo'lmadi.");
        }
    }

    @Override
    public Resource getResource(Long id) {
        Attach attach = getById(id);

        try {
            Path filePath = uploadPath.resolve(attach.getPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException ignored) {
            // no-op
        }

        throw new ResourceNotFoundException("Fayl topilmadi.");
    }

    @Override
    public void deleteByUrl(String attachUrl) {
        if (!StringUtils.hasText(attachUrl) || !attachUrl.startsWith(PUBLIC_PREFIX)) {
            return;
        }

        String idValue = attachUrl.substring(PUBLIC_PREFIX.length());
        try {
            Long id = Long.valueOf(idValue);
            Attach attach = getById(id);
            Files.deleteIfExists(uploadPath.resolve(attach.getPath()).normalize());
            attachRepository.delete(attach);
        } catch (NumberFormatException ignored) {
            // no-op
        } catch (IOException ignored) {
            // no-op
        }
    }

    private Attach getById(Long id) {
        return attachRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attach topilmadi."));
    }

    private AttachDTO toDTO(Attach attach) {
        return new AttachDTO(
                attach.getId(),
                attach.getOriginalName(),
                PUBLIC_PREFIX + attach.getId()
        );
    }
}
