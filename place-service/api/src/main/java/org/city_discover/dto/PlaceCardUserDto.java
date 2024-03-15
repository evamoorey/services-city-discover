package org.city_discover.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "ID автора должен быть заполнен")
    private UUID author;
}
