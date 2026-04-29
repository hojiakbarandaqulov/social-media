package org.example.service.impl;

import org.example.common.exception.BusinessException;
import org.example.common.exception.ResourceNotFoundException;
import org.example.dto.auth.AuthResponse;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.PasswordUpdateRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.enums.Role;
import org.example.entity.User;
import org.example.enums.UserStatus;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.example.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Bu email allaqachon ro'yxatdan o'tgan.");
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(savedUser), userMapper.toProfile(savedUser));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BusinessException("Email yoki parol noto'g'ri."));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Ushbu foydalanuvchi bloklangan.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );

        return new AuthResponse(jwtService.generateToken(user), userMapper.toProfile(user));
    }

    @Override
    @Transactional
    public void updatePassword(Long currentUserId, PasswordUpdateRequest request) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException("Joriy parol noto'g'ri.");
        }

        if (request.currentPassword().equals(request.newPassword())) {
            throw new BusinessException("Yangi parol joriy paroldan farq qilishi kerak.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
