package com.vigilan.backend.service.impl;

import com.vigilan.backend.entity.User;
import com.vigilan.backend.repository.UserRepository;
import com.vigilan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createIfNotExists(String userId, String email) {

        userRepository.findById(userId)
                .orElseGet(() ->
                        userRepository.save(
                                User.builder()
                                        .id(userId)
                                        .email(email)
                                        .build()
                        )
                );
    }
}