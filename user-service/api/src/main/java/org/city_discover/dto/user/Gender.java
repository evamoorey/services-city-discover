package org.city_discover.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {
    FEMALE("Женский"),
    MALE("Мужской");

    public String getCode() {
        return this.name();
    }

    private final String name;
}
