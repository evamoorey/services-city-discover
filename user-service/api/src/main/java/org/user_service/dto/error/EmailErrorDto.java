package org.user_service.dto.error;

import lombok.Data;

@Data
public class EmailErrorDto {
    String code = "400";
    String message = "Invalid email address";
}