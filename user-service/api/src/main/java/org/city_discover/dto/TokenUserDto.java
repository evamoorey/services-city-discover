package org.city_discover.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city_discover.dto.user.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUserDto {
    private UserDto user;
    private TokenDto token;
}
