package com.project.hack.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhotoDto {
    private String key;
    private String path;
}
