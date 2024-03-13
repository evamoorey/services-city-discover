package org.city_discover.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class MetaDto {
    private int page;
    private int size;
    private String afterValue;
    private List<String> sort;
    private long total;
    private long totalPages;
}

