package org.user_service.advice;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.BindingResult;
import org.user_service.dto.wrapper.CollectionResponseDto;
import org.user_service.dto.wrapper.ErrorDto;
import org.user_service.dto.wrapper.MetaDto;
import org.user_service.dto.wrapper.ResponseWrappedDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ResponseConverter {

    public static ResponseWrappedDto convertToResponseWrappedDto(Object obj) {

        final ResponseWrappedDto responseWrappedDto = new ResponseWrappedDto();
        responseWrappedDto.setStatus(true);
        responseWrappedDto.setErrors(Collections.emptyList());

        if (obj.getClass().isAssignableFrom(PageImpl.class)) {

            CollectionResponseDto collectionResponse = new CollectionResponseDto();

            MetaDto meta = new MetaDto();
            meta.setPage(((Page<?>) obj).getNumber());
            meta.setTotal(((Page<?>) obj).getTotalElements());
            meta.setSize(((Page<?>) obj).getSize());
            meta.setTotalPages(((Page<?>) obj).getTotalPages());

            collectionResponse.setData(((Page<?>) obj).getContent());
            collectionResponse.setMeta(meta);

            responseWrappedDto.setResponse(collectionResponse);

        } else {
            responseWrappedDto.setResponse(obj);
        }

        return responseWrappedDto;
    }

    public static ResponseWrappedDto responseWrappedFromErrorsDto(List<ErrorDto> errorList) {
        final ResponseWrappedDto responseWrappedDto = new ResponseWrappedDto();
        responseWrappedDto.setStatus(false);
        responseWrappedDto.setErrors(errorList);
        return responseWrappedDto;
    }

    public static ResponseWrappedDto convertToResponseWrappedDtoFromBindingResult(BindingResult bindingResult) {

        List<ErrorDto> errorsList = bindingResult.getAllErrors().stream().map((error) -> {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("ERROR");
            errorDto.setMessage(error.getDefaultMessage());
            return errorDto;
        }).collect(Collectors.toList());

        return responseWrappedFromErrorsDto(errorsList);
    }

    public static ResponseWrappedDto convertToResponseWrappedDtoFromErrorList(List<String> errors) {
        List<ErrorDto> errorDtoList = errors.stream().map(error -> {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("ERROR");
            errorDto.setMessage(error);
            return errorDto;
        }).toList();
        return responseWrappedFromErrorsDto(errorDtoList);
    }
}

