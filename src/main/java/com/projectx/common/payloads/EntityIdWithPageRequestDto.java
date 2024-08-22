package com.projectx.common.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityIdWithPageRequestDto {
    private Long entityId;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortParam;
    private String sortDir;
}
