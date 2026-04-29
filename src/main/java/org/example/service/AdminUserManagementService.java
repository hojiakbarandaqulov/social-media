package org.example.service;

import org.example.dto.admin.AdminUserResponse;
import org.example.dto.admin.AdminUserUpdateRequest;

import java.util.List;

public interface AdminUserManagementService {

    List<AdminUserResponse> getAllUsers();

    AdminUserResponse getUserById(Long userId);

    AdminUserResponse updateUser(Long adminUserId, Long targetUserId, AdminUserUpdateRequest request);

    void deactivateUser(Long adminUserId, Long targetUserId);
}
