package org.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.user_service.dto.UserUpdateDto;
import org.user_service.dto.wrapper.ErrorsMap;
import org.user_service.service.UserService;

import java.util.UUID;

import static org.user_service.utill.Converter.getErrorsMap;

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
        userService.update(userId, userUpdateDto);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
