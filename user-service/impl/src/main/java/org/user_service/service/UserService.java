package org.user_service.service;


import java.util.UUID;

public interface UserService {
    UUID create(String email);
}
