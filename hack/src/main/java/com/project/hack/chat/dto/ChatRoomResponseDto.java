package com.project.hack.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomResponseDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomData {
//        private Long chatId;
        private String senderNickName;
        private String senderProfileImg;
        private String receiverNickName;
        private String receiverProfileImg;
        private Long senderId;
        private Long chatRoomId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomList{
        private Long chatRoomId;
        private String otherProfileImg;
        private String otherNickName;
        private String lastChat;
        private String otherRegion;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomListData{
        private String result;
        private String msg;
        private String myImg;
        private String myNickname;
        private List<ChatRoomList> chatRoomList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageData{
        private String message;
        private String userNickname;
        private String messageModifiedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageListData{
        private String result;
        private String msg;
        private LocalDateTime lastDatetime;
        private String myProfileImg;
        private String myNickname;
        private String otherProfileImg;
        private String otherNickName;
        private int resultCount;
        private List<ChatMessageData> chatMessageDataList;
    }
}