package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.user_service.dto.user.UserDto;
import org.user_service.dto.user.UserPublicDto;
import org.user_service.dto.user.UserUpdateDto;
import org.user_service.entity.UserEntity;
import org.user_service.exception.NoSuchEntityException;
import org.user_service.exception.NotUniqueException;
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
    public UserDto create(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDto.class);
        }

        UUID userId = UUID.randomUUID();
        UserEntity newUser = UserEntity.builder()
                .id(userId)
                .email(email)
                .build();

        UserEntity inserted = userRepository.insert(newUser);
        return modelMapper.map(inserted, UserDto.class);
    }

    @Override
    public UserDto update(UUID id, UserUpdateDto userUpdateDto) {
        checkUsername(userUpdateDto.getUsername());

        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("Пользователь не существует");
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

    @Override
    public UserDto findPrivateBy(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("Пользователь не существует");
        });

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserPublicDto findBy(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("Пользователь не существует");
        });

        return modelMapper.map(user, UserPublicDto.class);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }

    private void checkUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            log.error("Username is taken by another user");
            throw new NotUniqueException("Username уже используется другим пользователем");
        }
    }

    private boolean equalHashes(UserEntity userFromDb, UserEntity updatedUser) {
        return userFromDb.hashCode() == updatedUser.hashCode();
    }
}
