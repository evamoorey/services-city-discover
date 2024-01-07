package org.user_service.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.user_service.dto.wrapper.ErrorDto;
import org.user_service.dto.wrapper.ResponseWrappedDto;

import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class DefaultAdvice {
    private final ErrorList errorList;

    public DefaultAdvice(ErrorList errorList) {
        this.errorList = errorList;
    }

    @ExceptionHandler({})
    public ResponseEntity<?> handleException(RuntimeException e) {
        log.info("Exception '{}' is handled. Message: '{}'.",
                e.getClass().getName(),
                e.getMessage());

        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode("ERROR");
        errorDto.setMessage(e.getMessage());

        ResponseWrappedDto responseWrappedDto;
        if (errorList.isEmpty()) {
            responseWrappedDto = ResponseConverter.responseWrappedFromErrorsDto(Collections.singletonList(errorDto));
        } else {
            responseWrappedDto = ResponseConverter.convertToResponseWrappedDtoFromErrorList(errorList);
        }

        return new ResponseEntity<>(responseWrappedDto, HttpStatus.BAD_REQUEST);
    }
}
