package org.user_service.dto.wrapper;

import lombok.Data;

@Data
public class ErrorDto {
    String code;
    String message;
}