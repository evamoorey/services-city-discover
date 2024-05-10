package org.city_discover.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KittyCreateDto {

    @NotBlank(message = "Имя должно быть заполнено")
    private String name;

    @NotBlank(message = "Координаты должны быть заполнены")
    private Double latitude;

    @NotBlank(message = "Координаты должны быть заполнены")
    private Double longitude;

    @NotBlank(message = "ID фото должно быть заполнено")
    private UUID photoId;
}
