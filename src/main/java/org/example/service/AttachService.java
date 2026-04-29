package org.example.service;

import org.example.dto.attach.AttachDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {

    AttachDTO uploadFile(MultipartFile file);

    Resource getResource(String id);

    void deleteByUrl(String attachUrl);
}
