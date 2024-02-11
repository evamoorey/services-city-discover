package org.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.user_service.dto.AuthCodeDto;
import org.user_service.dto.TokenDto;
import org.user_service.dto.TokenUserDto;
import org.user_service.dto.error.EmailErrorDto;
import org.user_service.dto.wrapper.ErrorsMap;
import org.user_service.service.AuthService;

import static org.user_service.utill.Converter.getErrorsMap;
import static org.user_service.utill.EmailChecker.isValidEmail;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<?> sendCode(String email) {
        if (!isValidEmail(email)) {
            return new ResponseEntity<>(new EmailErrorDto(), HttpStatus.BAD_REQUEST);
        }

        authService.sendEmailCode(email);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> login(AuthCodeDto authCodeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        TokenUserDto tokenUserDto = authService.login(authCodeDto);

        return new ResponseEntity<>(tokenUserDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> refresh(String refresh) {
        TokenDto tokens = authService.refresh(refresh);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
}
