package com.vigilan.backend.controller;

import com.vigilan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Called immediately after login
    @PostMapping("/me")
    public ResponseEntity<Void> registerOrSyncUser(
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userId = jwt.getSubject();   // Supabase UUID
        String email = jwt.getClaim("email");

        userService.createIfNotExists(userId, email);

        return ResponseEntity.ok().build();
    }
}