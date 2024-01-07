package org.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Email должен быть заполнен")
    private String email;

    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String username;
}
