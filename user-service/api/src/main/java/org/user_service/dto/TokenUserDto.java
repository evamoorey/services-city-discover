package org.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.user_service.dto.user.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUserDto {
    private UserDto user;
    private TokenDto token;
}
