package org.city_discover.dto.error;

import lombok.Data;

@Data
public class EmailErrorDto {
    String code = "400";
    String message = "Invalid email address";
}