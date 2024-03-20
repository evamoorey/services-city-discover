package org.city_discover.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city_discover.dto.Preference;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String username;

    private Gender gender;
    private Integer age;
    private List<Preference> preferences;
}
