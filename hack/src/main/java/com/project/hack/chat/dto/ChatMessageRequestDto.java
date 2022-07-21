package com.project.hack.chat.dto;
import lombok.*;

public class ChatMessageRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Write{
        private Long userId;
        private Long chatRoomId;
        private String modifiedAt;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WriteSubscriber{

        private Long userId;
        private String userNickname;
        private String modifiedAt;
        private Long chatRoomId;
        private String message;
    }
}
