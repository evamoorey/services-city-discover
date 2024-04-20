package org.city_discover.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameKittyDto {
    private UUID id;

    @NotBlank(message = "Название должно быть заполнено")
    private String name;

    private String description;
    private String author;
}
