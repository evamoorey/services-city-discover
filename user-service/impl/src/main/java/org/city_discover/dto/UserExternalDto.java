package org.city_discover.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.city_discover.dto.user.Gender;

import java.util.List;
import java.util.UUID;

@Data
public class UserExternalDto {
    @JsonProperty("user_id")
    private UUID id;

    private Integer age;
    private String gender;
    private List<Preference> preferences;
}
