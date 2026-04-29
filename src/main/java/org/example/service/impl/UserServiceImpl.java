package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
import org.example.dto.user.ProfileResponse;
import org.example.dto.user.UpdateProfileRequest;
import org.example.entity.User;
import org.example.enums.UserStatus;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.AvatarStorageService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AvatarStorageService avatarStorageService;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            AvatarStorageService avatarStorageService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.avatarStorageService = avatarStorageService;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getCurrentProfile(Long currentUserId) {
        return userMapper.toProfile(getActiveUser(currentUserId));
    }

    @Override
    @Transactional
    public ProfileResponse updateCurrentProfile(Long currentUserId, UpdateProfileRequest request) {
        User user = getActiveUser(currentUserId);
        String normalizedEmail = request.email().trim().toLowerCase();

        if (!user.getEmail().equalsIgnoreCase(normalizedEmail) && userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Bu email allaqachon boshqa foydalanuvchiga biriktirilgan.");
        }

        user.setFullName(request.fullName().trim());
        user.setEmail(normalizedEmail);
        user.setBio(request.bio());

        return userMapper.toProfile(userRepository.save(user));
    }

    @Override
    @Transactional
    public ProfileResponse updateAvatar(Long currentUserId, MultipartFile file) {
        User user = getActiveUser(currentUserId);
        avatarStorageService.deleteIfExists(user.getAvatarUrl());
        user.setAvatarUrl(avatarStorageService.store(file));
        return userMapper.toProfile(userRepository.save(user));
    }

    private User getActiveUser(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Bloklangan foydalanuvchi bu amalni bajara olmaydi.");
        }

        return user;
    }
}
