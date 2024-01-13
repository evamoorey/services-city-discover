package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.user_service.entity.UserEntity;
import org.user_service.repository.UserRepository;
import org.user_service.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UUID create(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get().getId();
        }

        UUID userId = UUID.randomUUID();
        UserEntity newUser = UserEntity.builder()
                .id(userId)
                .email(email)
                .build();

        userRepository.insert(newUser);
        return userId;
    }
}
