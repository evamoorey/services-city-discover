package org.city_discover.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCardUserDto {

    @NotBlank(message = "Название места должно быть заполнено")
    private String name;

    private String description;

    @NotNull(message = "Координаты места должны быть заполнены")
    private Double latitude;

    @NotNull(message = "Координаты места должны быть заполнены")
    private Double longitude;
}
