package org.city_discover.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PlaceEntity {
    private UUID id;
    private String name;
    private String description;
    private String author;
    private Double latitude;
    private Double longitude;
    private Instant creationDate;
    private Instant modificationDate;
    private List<String> photoId;
}
