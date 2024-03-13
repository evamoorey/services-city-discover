package org.city_discover.dto.wrapper;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrappedDto {
    private List<ErrorDto> errors;
    private Object response;
    private boolean status;
}

