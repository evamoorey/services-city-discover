package org.city_discover.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.AuthCodeDto;
import org.city_discover.dto.TokenDto;
import org.city_discover.dto.TokenUserDto;
import org.city_discover.dto.error.EmailErrorDto;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.city_discover.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import static org.city_discover.utill.Converter.getErrorsMap;
import static org.city_discover.utill.EmailChecker.isValidEmail;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

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
