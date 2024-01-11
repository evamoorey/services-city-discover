package org.user_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.user_service.dto.AuthCodeDto;
import org.user_service.dto.UserCreateDto;
import org.user_service.dto.wrapper.ErrorDto;
import org.user_service.dto.wrapper.ErrorsMap;
import org.user_service.service.AuthService;

import static org.user_service.utill.Converter.getErrorsMap;
import static org.user_service.utill.EmailChecker.isValidEmail;

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
        if (!isValidEmail(email)) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Invalid email address");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        log.info("Send auth code to: {}", email);
        authService.sendEmailCode(email);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> checkCode(AuthCodeDto authCodeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            log.info("Errors in input dto for check code {}", errorsMap);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        if (!isValidEmail(authCodeDto.getEmail())) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Invalid email address");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        log.info("Check auth code for: {}", authCodeDto.getEmail());

        return new ResponseEntity<>(authService.checkEmailCode(authCodeDto), HttpStatus.OK);
    }
}
