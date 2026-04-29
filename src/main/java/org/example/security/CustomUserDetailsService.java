package org.example.security;

import org.example.common.exception.ResourceNotFoundException;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));
        return new CustomUserPrincipal(user);
    }
}
