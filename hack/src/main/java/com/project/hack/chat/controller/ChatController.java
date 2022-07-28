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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

        // modifiedAt을 형식에 맞춰 보내기 위한 코드
        String[] messageModifiedAt = message.getModifiedAt().split("T");
        String date = messageModifiedAt[0];
        String time = messageModifiedAt[1];
        String[] dateList = date.split("-");
        String year = dateList[0];
        String month = dateList[1];
        String day = dateList[2];
        System.out.println(year +"/"+month+"/"+day);
        String[] timeList = time.split(":");
        int hour = Integer.parseInt(timeList[0]);
        String min = timeList[1];
        System.out.println("hour : " +hour + " min : "+min);
        String messagedModifiedAt;
        if(hour>12){messagedModifiedAt = year+"/" + month+"/"+day + " " + (hour-12)+":"+min+"PM";}
        else{messagedModifiedAt = year +"/" + month+"/"+day + " " + hour+":"+min+"AM";}

        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chatRoom(chatRoom)
                .writer(user)
                .messageModifiedAt(messagedModifiedAt)
                .build();


//        Websocket에 발행된 메시지를 redis로 발행(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                ChatMessageRequestDto.WriteSubscriber.builder()
                        .userId(message.getUserId())
                        .userNickname(user.getNickname())
                        .messageModifiedAt(messagedModifiedAt)
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
