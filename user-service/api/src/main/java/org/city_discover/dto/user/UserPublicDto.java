package org.city_discover.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicDto {
    private UUID id;
    private String username;
}
