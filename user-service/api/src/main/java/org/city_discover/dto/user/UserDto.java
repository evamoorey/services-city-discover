package org.city_discover.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city_discover.dto.Preference;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String username;
    private Gender gender;
    private Integer age;
    private List<Preference> preferences;
}
