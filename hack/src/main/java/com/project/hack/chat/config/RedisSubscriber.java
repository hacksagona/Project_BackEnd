package com.project.hack.chat.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.chat.dto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    //Redis에서 메시지가 발행(Publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
    public void sendMessage(String publishMessage){
        try {
            System.out.println("RedisSubscriber에서 보내는 메시지");
            System.out.println(publishMessage);
            // ChatMessage 객채로 맵핑
//            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            ChatMessageRequestDto.WriteSubscriber chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequestDto.WriteSubscriber.class);

            System.out.println("sub 메시지 확인");
            System.out.println(chatMessage);

            messagingTemplate.convertAndSend("/sub/chat/room" + chatMessage.getChatRoomId(), chatMessage);
            System.out.println("발송 요청 완료");


        } catch (Exception e) {
            System.out.println("Exception {} "+ e);
        }
    }

}
