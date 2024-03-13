package org.city_discover.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SubscriptionDto {
    private UUID id;
    private String username;
}
