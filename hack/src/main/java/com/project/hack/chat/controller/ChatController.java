package com.project.hack.chat.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.chat.dto.ChatMessageRequestDto;
import com.project.hack.chat.model.ChatMessage;
import com.project.hack.chat.model.ChatRoom;
import com.project.hack.chat.repository.ChatMessageRepository;
import com.project.hack.chat.repository.ChatRoomRepository;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessageSendingOperations sendingOperations;

    // 웹소켓으로 들어오는 메시지 발행 처리 -> 클라이언트에서는 /pub/templates/chat/message로 발행 요청
    @MessageMapping("/templates/chat/message")
    @Transactional
    public String message(ChatMessageRequestDto.Write message){

        System.out.println("채팅 보내는 클래스 들어옴");

        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId()).orElseThrow(()->new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
        User user = userRepository.findById(message.getUserId()).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chatRoom(chatRoom)
                .writer(user)
                .build();

//        Websocket에 발행된 메시지를 redis로 발행(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                ChatMessageRequestDto.WriteSubscriber.builder()
                        .userId(message.getUserId())
                        .userNickname(user.getNickname())
                        .modifiedAt(message.getModifiedAt())
                        .chatRoomId(message.getChatRoomId())
                        .message(message.getMessage())
                        .build());
        System.out.println("메시지 redis 발행 완료");
        // 룸 modifiedAt
        Long chatMessageId = chatMessageRepository.save(chatMessage).getId();
        System.out.println("chatMessageId : " + chatMessageId);
        ChatMessage getMessage = chatMessageRepository.findById(chatMessageId).orElseThrow(()->new CustomException(ErrorCode.CHATMESSAGE_NOT_FOUND));

        //chatRoom 수정시간 병경
        chatRoom.setModifiedAt(getMessage.getModifiedAt());
        System.out.println("채팅 수정시간 변경 완료");

//        sendingOperations.convertAndSend("/sub/chat/room/"+message.getChatRoomId(),message);
//        System.out.println("메시지 전송까지 했음 : " +message.getMessage());
        return "킹준호";
    }

}
