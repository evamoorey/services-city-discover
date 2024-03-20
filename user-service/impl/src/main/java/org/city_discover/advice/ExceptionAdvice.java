package org.city_discover.advice;

import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.wrapper.ErrorDto;
import org.city_discover.dto.wrapper.ResponseWrappedDto;
import org.city_discover.exception.*;
import org.jooq.exception.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    private final ErrorList errorList;

    public ExceptionAdvice(ErrorList errorList) {
        this.errorList = errorList;
    }

    @ExceptionHandler({NoSuchEntityException.class,
            TooMuchRequestsException.class,
            NotUniqueException.class,
            UnprocessableActionException.class})
    public ResponseEntity<?> handleBadRequestException(RuntimeException e) {
        ResponseWrappedDto responseWrappedDto = buildResponseWrappedDtoFromException(e);

        return new ResponseEntity<>(responseWrappedDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<?> handleUnauthorizedException(RuntimeException e) {
        ResponseWrappedDto responseWrappedDto = buildResponseWrappedDtoFromException(e);

        return new ResponseEntity<>(responseWrappedDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DataAccessException.class,
            ExternalServiceException.class})
    public ResponseEntity<?> handleExternalServiceException(RuntimeException e) {
        ResponseWrappedDto responseWrappedDto = buildResponseWrappedDtoFromException(e);

        return new ResponseEntity<>(responseWrappedDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseWrappedDto buildResponseWrappedDtoFromException(RuntimeException e) {

        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode("ERROR");
        errorDto.setMessage(e.getMessage());

        ResponseWrappedDto responseWrappedDto;
        if (errorList.isEmpty()) {
            responseWrappedDto = ResponseConverter.responseWrappedFromErrorsDto(Collections.singletonList(errorDto));
        } else {
            responseWrappedDto = ResponseConverter.convertToResponseWrappedDtoFromErrorList(errorList);
        }

        return responseWrappedDto;
    }
}
