package org.city_discover.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserPublicDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.city_discover.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.city_discover.utill.Converter.getErrorsMap;

@RestController
@AllArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<?> update(UserUpdateDto userUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        UserDto user = userService.update(userId, userUpdateDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> findInfo() {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        UserDto user = userService.findPrivateBy(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserPublicDto> findById(UUID id) {
        UserPublicDto user = userService.findBy(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<UserPublicDto>> findAll(String username, Pageable pageable) {
        Page<UserPublicDto> users = userService.findAll(username, pageable);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> delete() {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userService.delete(userId);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
