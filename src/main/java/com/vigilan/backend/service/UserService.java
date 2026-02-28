package com.vigilan.backend.service;

public interface UserService {
    void createIfNotExists(String userId, String email);
}