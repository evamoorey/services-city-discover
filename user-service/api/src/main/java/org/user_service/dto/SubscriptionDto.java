package org.user_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SubscriptionDto {
    private UUID id;
    private String username;
}
