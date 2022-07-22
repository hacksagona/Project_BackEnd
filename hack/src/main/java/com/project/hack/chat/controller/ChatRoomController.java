package com.project.hack.chat.controller;

import com.project.hack.chat.dto.ChatRoomRequestDto;
import com.project.hack.chat.dto.ChatRoomResponseDto;
import com.project.hack.chat.service.ChatRoomService;
import com.project.hack.model.User;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final TokenValidator tokenValidator;

    @PostMapping("/room")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomData createRoom(@RequestBody ChatRoomRequestDto.Create create, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        tokenValidator.userIdCompareToken(create.getBuyerId(), user.getId());
        return chatRoomService.createChatRoom(create);

    }

    //채팅 리스트 받아오기
    @GetMapping("/room/{userId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomListData chatList(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tokenValidator.userIdCompareToken(userId,userDetails.getUser().getId());
        return chatRoomService.findChatList(userId);
    }

    //해당 채팅방 채팅내용 반환

    @GetMapping("/roomlist/{userId}/chatRoom/{chatRoomId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatMessageListData roomChatList(@PathVariable Long userId, @PathVariable Long chatRoomId,
                                                                @RequestParam(value = "time",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime localDateTime,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        tokenValidator.userIdCompareToken(userId,userDetails.getUser().getId());
        return chatRoomService.roomChatListService(userId, chatRoomId, localDateTime);

    }
}
