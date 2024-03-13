package org.city_discover.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class AuthCodeEntity {
    private String email;
    private String code;
    private Instant creationDate;
}
