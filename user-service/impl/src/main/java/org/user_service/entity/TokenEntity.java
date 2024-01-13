package org.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TokenEntity {
    private UUID userId;
    private String refreshToken;
}
