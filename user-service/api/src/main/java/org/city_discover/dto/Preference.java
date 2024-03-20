package org.city_discover.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preference {
    @JsonProperty("subcategory_id")
    private String subcategory;

    private String mark;
}
