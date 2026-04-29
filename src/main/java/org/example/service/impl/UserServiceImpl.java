package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
<<<<<<< HEAD
import org.example.dto.attach.AttachDTO;
=======
>>>>>>> 048d98a97a63f72eef6878406674d08776bd8fd3
import org.example.dto.user.ProfileResponse;
import org.example.dto.user.UpdateProfileRequest;
import org.example.entity.User;
import org.example.enums.UserStatus;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
<<<<<<< HEAD
import org.example.service.AttachService;
=======
import org.example.service.AvatarStorageService;
>>>>>>> 048d98a97a63f72eef6878406674d08776bd8fd3
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
<<<<<<< HEAD
    private final AttachService attachService;
=======
    private final AvatarStorageService avatarStorageService;
>>>>>>> 048d98a97a63f72eef6878406674d08776bd8fd3

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
<<<<<<< HEAD
            AttachService attachService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.attachService = attachService;
=======
            AvatarStorageService avatarStorageService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.avatarStorageService = avatarStorageService;
>>>>>>> 048d98a97a63f72eef6878406674d08776bd8fd3
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
<<<<<<< HEAD
        attachService.deleteByUrl(user.getAvatarUrl());
        AttachDTO uploadedFile = attachService.uploadFile(file);
        user.setAvatarUrl(uploadedFile.url());
=======
        avatarStorageService.deleteIfExists(user.getAvatarUrl());
        user.setAvatarUrl(avatarStorageService.store(file));
>>>>>>> 048d98a97a63f72eef6878406674d08776bd8fd3
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
