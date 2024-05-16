package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserPublicDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.city_discover.entity.UserEntity;
import org.city_discover.exception.NoSuchEntityException;
import org.city_discover.exception.NotUniqueException;
import org.city_discover.repository.UserRepository;
import org.city_discover.service.RecommendationExternalService;
import org.city_discover.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.city_discover.utill.EntityConverter.mapUserEntityToUserDto;
import static org.city_discover.utill.EntityConverter.mapUserUpdateDtoToUserEntity;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RecommendationExternalService recommendationExternalService;

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
        return mapUserEntityToUserDto(inserted);
    }

    @Override
    @Transactional
    public UserDto update(UUID id, UserUpdateDto userUpdateDto) {
        checkUsername(userUpdateDto.getUsername());

        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("Пользователь не существует");
        });

        UserEntity toUpdate = mapUserUpdateDtoToUserEntity(userUpdateDto);
        toUpdate.setId(user.getId());
        toUpdate.setEmail(user.getEmail());
        toUpdate.setCreationDate(user.getCreationDate());

        if (!equalHashes(user, toUpdate)) {
            UserEntity updated = userRepository.update(toUpdate);

            recommendationExternalService.saveUser(updated);
            return mapUserEntityToUserDto(updated);
        }

        return mapUserEntityToUserDto(user);
    }

    @Override
    public UserDto findPrivateBy(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            log.error("No such user with id: [{}]", id);
            return new NoSuchEntityException("Пользователь не существует");
        });

        return mapUserEntityToUserDto(user);
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
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("No such user with id: [{}]", userId);
            return new NoSuchEntityException("Пользователь не существует");
        });

        userRepository.delete(userId);
    }

    @Override
    public Page<UserPublicDto> findAll(String username, Pageable pageable) {
        return userRepository.findAll(username, pageable);

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
