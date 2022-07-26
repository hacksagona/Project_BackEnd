package com.project.hack.chat.dto;

import lombok.*;

public class ChatRoomRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class Create {
        private Long senderId;
        private Long receiverId;
    }
}
