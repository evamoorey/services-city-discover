package org.city_discover.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCardDto {

    @NotBlank(message = "Название места должно быть заполнено")
    private String name;

    private String description;
    private String author;
    private Double latitude;
    private Double longitude;
}
