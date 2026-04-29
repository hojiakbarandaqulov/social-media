package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
import org.example.dto.admin.AdminUserResponse;
import org.example.dto.admin.AdminUserUpdateRequest;
import org.example.enums.Role;
import org.example.entity.User;
import org.example.enums.UserStatus;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.AdminUserManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminUserManagementServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(userMapper::toAdmin)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse getUserById(Long userId) {
        return userMapper.toAdmin(getUser(userId));
    }

    @Override
    @Transactional
    public AdminUserResponse updateUser(Long adminUserId, Long targetUserId, AdminUserUpdateRequest request) {
        User targetUser = getUser(targetUserId);
        String normalizedEmail = request.email().trim().toLowerCase();

        if (!targetUser.getEmail().equalsIgnoreCase(normalizedEmail) && userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Bu email allaqachon boshqa foydalanuvchiga tegishli.");
        }

        if (adminUserId.equals(targetUserId) && (request.role() != Role.ADMIN || request.status() != UserStatus.ACTIVE)) {
            throw new BusinessException("Admin o'zini USER qilib yoki bloklab qo'ya olmaydi.");
        }

        targetUser.setFullName(request.fullName().trim());
        targetUser.setEmail(normalizedEmail);
        targetUser.setBio(request.bio());
        targetUser.setRole(request.role());
        targetUser.setStatus(request.status());

        return userMapper.toAdmin(userRepository.save(targetUser));
    }

    @Override
    @Transactional
    public void deactivateUser(Long adminUserId, Long targetUserId) {
        if (adminUserId.equals(targetUserId)) {
            throw new BusinessException("Admin o'z akkauntini bloklay olmaydi.");
        }

        User user = getUser(targetUserId);
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));
    }
}
