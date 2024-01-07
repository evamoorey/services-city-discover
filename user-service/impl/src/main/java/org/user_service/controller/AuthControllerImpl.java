package org.user_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.user_service.dto.UserCreateDto;
import org.user_service.dto.wrapper.ErrorDto;
import org.user_service.service.AuthService;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<?> create(UserCreateDto userCreateDto, BindingResult bindingResult) {
        return null;
    }

    @Override
    public ResponseEntity<?> sendCode(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Invalid email address");
            return new ResponseEntity<>(errorDto, HttpStatus.OK);
        }

        log.info("Send auth code to: {}", email);
        authService.sendEmailCode(email);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> checkCode(String email) {
        return null;
    }
}
