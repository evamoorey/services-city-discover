package org.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UserEntity {
    private UUID id;
    private String email;
    private String username;
    private Instant creationDate;
}
