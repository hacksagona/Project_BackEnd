package com.project.hack.dto.response;


import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPageResponseDto {

    private boolean isLast;
    private int pageTotalSize;
    private List<PostResponseDto> postResponseDtoList;

}
