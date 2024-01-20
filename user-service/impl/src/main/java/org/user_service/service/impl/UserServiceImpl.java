package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.user_service.dto.UserDto;
import org.user_service.dto.UserUpdateDto;
import org.user_service.entity.UserEntity;
import org.user_service.exception.NoSuchEntityException;
import org.user_service.repository.UserRepository;
import org.user_service.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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

    @Override
    public UserDto update(UUID id, UserUpdateDto userUpdateDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("No such user with id: [%s]".formatted(id));
        });

        UserEntity toUpdate = modelMapper.map(userUpdateDto, UserEntity.class);
        toUpdate.setId(user.getId());
        toUpdate.setEmail(user.getEmail());
        toUpdate.setCreationDate(user.getCreationDate());

        if (!equalHashes(user, toUpdate)) {
            UserEntity updated = userRepository.update(toUpdate);
            return modelMapper.map(updated, UserDto.class);
        }

        return modelMapper.map(user, UserDto.class);
    }

    private boolean equalHashes(UserEntity userFromDb, UserEntity updatedUser) {
        return userFromDb.hashCode() == updatedUser.hashCode();
    }
}
