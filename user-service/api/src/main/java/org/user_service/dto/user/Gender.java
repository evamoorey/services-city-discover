package org.user_service.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {
    FEMALE("Female"),
    MALE("Male");

    public String getCode() {
        return this.name();
    }

    private final String name;
}
