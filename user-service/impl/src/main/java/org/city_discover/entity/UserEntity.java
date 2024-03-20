package org.city_discover.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserEntity {
    private UUID id;
    private String email;
    private String username;
    private String gender;
    private Integer age;
    private String preferences;
    private Instant creationDate;
}
