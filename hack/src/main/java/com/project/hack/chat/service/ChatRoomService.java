package com.project.hack.chat.service;


import com.project.hack.chat.dto.ChatRoomRequestDto;
import com.project.hack.chat.dto.ChatRoomResponseDto;
import com.project.hack.chat.model.ChatMessage;
import com.project.hack.chat.model.ChatRoom;
import com.project.hack.chat.repository.ChatMessageRepository;
import com.project.hack.chat.repository.ChatRoomRepository;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.PostRepository;
import com.project.hack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
//    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    //채팅방 생성
    @Transactional
    public ChatRoomResponseDto.ChatRoomData createChatRoom(@RequestBody ChatRoomRequestDto.Create create) {
        User buyer = userRepository.findById(create.getBuyerId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User seller = userRepository.findById(create.getSellerId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //이미 있는지 확인
        ChatRoom chatRoomExistCheck = chatRoomRepository.findByBuyerAndSeller(buyer,seller);
        if(chatRoomExistCheck!=null){
            return ChatRoomResponseDto.ChatRoomData.builder().chatRoomId(chatRoomExistCheck.getId()).build();
        }

        ChatRoom chatRoom = ChatRoom.create(ChatRoom.builder()
                .buyer(buyer)
                .seller(seller)
                .build());

        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();

        ChatRoomResponseDto.ChatRoomData chatRoomData = ChatRoomResponseDto.ChatRoomData.builder()
                .sellerNickName(chatRoom.getSeller().getNickname())
                .buyerNickName(chatRoom.getBuyer().getNickname())
                .sellerProfileImg(chatRoom.getSeller().getProfile_img())
                .buyerProfileImg(chatRoom.getBuyer().getProfile_img())
                .chatRoomId(chatRoomId)
                .buyerId(create.getBuyerId())
                .build();

        return chatRoomData;

    }

    @Transactional
    public ChatRoomResponseDto.ChatRoomListData findChatList(Long userId){

        //내 정보 가져오기
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
//        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUserId(userId);
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<ChatRoomResponseDto.ChatRoomList> chatRoomListList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList){
            if(Objects.equals(userId, chatRoom.getBuyer().getId())){
                String lastChat = "";
                if(chatRoom.getChatMessageList().size()==0){
                    lastChat = "채팅이 없습니다.";
                }else {
                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size()-1).getMessage();
                }
                ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                        .chatRoomId(chatRoom.getId())
                        .otherProfileImg(chatRoom.getSeller().getProfile_img())
                        .otherNickName(chatRoom.getSeller().getNickname())
                        .modifiedAt(chatRoom.getModifiedAt())
                        .lastChat(lastChat)
                        .build();
                chatRoomListList.add(chatRoomListBuilder);
            }else {
                String lastChat = "";
                if(chatRoom.getChatMessageList().size()==0){
                    lastChat = "채팅이 없습니다.";
                }else {
                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size()-1).getMessage();
                }
                ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                        .chatRoomId(chatRoom.getId())
                        .otherProfileImg(chatRoom.getBuyer().getProfile_img())
                        .otherNickName(chatRoom.getBuyer().getNickname())
                        .modifiedAt(chatRoom.getModifiedAt())
                        .lastChat(lastChat)
                        .build();
                chatRoomListList.add(chatRoomListBuilder);
            }
        }
        return ChatRoomResponseDto.ChatRoomListData.builder()
                .result("success")
                .msg("전체 채팅 리스트 조회 성공")
                .myImg(user.getProfile_img())
                .myNickname(user.getNickname())
                .chatRoomList(chatRoomListList)
                .build();

    }

//    public ChatRoomResponseDto.ChatMessageListData roomChatListService(Long userId, Long chatRoomId, LocalDateTime localDateTime) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
//                ()-> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
//
//        User another = new User();
//
//
//        //내가 buyer인지 seller인지 구별하기 위한 코드
//        if (Objects.equals(chatRoom.getBuyer().getId(), userId)){
//            another = chatRoom.getSeller();
//        }else {
//            another = chatRoom.getBuyer();
//        }
//
//        LocalDateTime lastDateTime = null;
//        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomOrderByModifiedAt(chatRoomId,localDateTime);
//
//        int resultCount = chatMessageList.size();
//
//        if(resultCount>=1){
//            lastDateTime = chatMessageList.get(resultCount-1).getModifiedAt();
//        }
//
//        List<ChatRoomResponseDto.ChatMessageData> chatMessageDataList = new ArrayList<>();
//        for( ChatMessage chatMessage : chatMessageList){
//            chatMessageDataList.add(ChatRoomResponseDto.ChatMessageData.builder()
//                    .message(chatMessage.getMessage())
//                    .userNickname(chatMessage.getWriter().getNickname())
//                    .modifiedAt(chatMessage.getModifiedAt())
//                    .build());
//
//        }
//        return ChatRoomResponseDto.ChatMessageListData.builder()
//                .result("success")
//                .msg("해당 채팅방 채팅 내용 반환 성공")
//                .resultCount(resultCount)
//                .lastDatetime(lastDateTime)
//                .otherProfileImg(another.getProfile_img())
//                .otherNickName(another.getNickname())
//                .chatMessageDataList(chatMessageDataList)
//                .build();
//
//    }
}

