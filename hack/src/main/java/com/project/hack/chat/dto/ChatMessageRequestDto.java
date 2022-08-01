package com.project.hack.chat.dto;
import lombok.*;

import javax.persistence.Column;

public class ChatMessageRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Write{
        private Long userId;
        private Long receiverId;
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
        private String messageModifiedDate;
        private String messageModifiedTime;

        private Long chatRoomId;
        private String message;
    }
}
