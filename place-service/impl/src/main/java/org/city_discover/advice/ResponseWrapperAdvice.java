package org.city_discover.advice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.controller.PlaceController;
import org.city_discover.controller.PlacePhotoController;
import org.city_discover.dto.wrapper.ResponseWrappedDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(assignableTypes = {PlaceController.class})
@Slf4j
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter methodParameter,
                                  @NonNull MediaType mediaType,
                                  @NonNull Class aClass,
                                  @NonNull ServerHttpRequest serverHttpRequest,
                                  @NonNull ServerHttpResponse serverHttpResponse) {

        if (body == null) {
            return null;
        }

        if (body instanceof ResponseWrappedDto) {
            return body;
        }

        return ResponseConverter.convertToResponseWrappedDto(body);
    }
}