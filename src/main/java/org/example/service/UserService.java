package org.example.service;

import org.example.dto.user.ProfileResponse;
import org.example.dto.user.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ProfileResponse getCurrentProfile(Long currentUserId);

    ProfileResponse updateCurrentProfile(Long currentUserId, UpdateProfileRequest request);

    ProfileResponse updateAvatar(Long currentUserId, MultipartFile file);
}
