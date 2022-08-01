package com.project.hack.chat.service;


import com.project.hack.chat.dto.ChatRoomRequestDto;
import com.project.hack.chat.dto.ChatRoomResponseDto;
import com.project.hack.chat.model.ChatMessage;
import com.project.hack.chat.model.ChatRoom;
import com.project.hack.chat.model.Notice;
import com.project.hack.chat.repository.ChatMessageRepository;
import com.project.hack.chat.repository.ChatRoomRepository;
import com.project.hack.chat.repository.NoticeRepository;
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
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    //채팅방 생성
    @Transactional
    public ChatRoomResponseDto.ChatRoomData createChatRoom(@RequestBody ChatRoomRequestDto.Create create) {
        User sender = userRepository.findById(create.getSenderId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(create.getReceiverId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        System.out.println("senderId : " + sender.getId());
        System.out.println("receiverId : " + receiver.getId());
        //이미 있는지 확인
        ChatRoom chatRoomExistCheck = chatRoomRepository.findBySenderAndReceiver(sender,receiver);
        if(chatRoomExistCheck!=null){
            return ChatRoomResponseDto.ChatRoomData.builder().receiverNickName(chatRoomExistCheck.getReceiver().getNickname())
                    .senderNickName(chatRoomExistCheck.getSender().getNickname())
                    .receiverProfileImg(chatRoomExistCheck.getReceiver().getProfile_img())
                    .senderProfileImg(chatRoomExistCheck.getSender().getProfile_img())
                    .chatRoomId(chatRoomExistCheck.getId())
                    .senderId(create.getSenderId())
                    .build();
        }

        ChatRoom chatRoom = ChatRoom.create(ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build());

        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();
        System.out.println("채팅방 id : " + chatRoomId);

        ChatRoomResponseDto.ChatRoomData chatRoomData = ChatRoomResponseDto.ChatRoomData.builder()
                .receiverNickName(chatRoom.getReceiver().getNickname())
                .senderNickName(chatRoom.getSender().getNickname())
                .receiverProfileImg(chatRoom.getReceiver().getProfile_img())
                .senderProfileImg(chatRoom.getSender().getProfile_img())
                .chatRoomId(chatRoomId)
                .senderId(create.getSenderId())
                .build();

        return chatRoomData;

    }

    @Transactional
    public ChatRoomResponseDto.ChatRoomListData findChatList(Long userId){

        //내 정보 가져오기
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUserId(userId);
        List<ChatRoomResponseDto.ChatRoomList> chatRoomListList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList){
            if(Objects.equals(userId, chatRoom.getSender().getId())){
                String lastChat = "";
                if(chatRoom.getChatMessageList().size()==0){
                    lastChat = "채팅이 없습니다.";
                }else {
                    boolean notice = noticeRepository.findByUserIdAndChatRoomId(chatRoom.getSender().getId(), chatRoom.getId()).isPresent();

                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size()-1).getMessage();
                    ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                            .chatRoomId(chatRoom.getId())
                            .otherProfileImg(chatRoom.getReceiver().getProfile_img())
                            .otherNickName(chatRoom.getReceiver().getNickname())
                            .modifiedAt(chatRoom.getModifiedAt())
                            .lastChat(lastChat)
                            .isNotice(notice)
                            .build();
                    chatRoomListList.add(chatRoomListBuilder);
                }

            }else {
                String lastChat = "";
                if(chatRoom.getChatMessageList().size()==0){
                    lastChat = "채팅이 없습니다.";
                }else {
                    boolean notice = noticeRepository.findByUserIdAndChatRoomId(chatRoom.getReceiver().getId(), chatRoom.getId()).isPresent();

                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size()-1).getMessage();
                    ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                            .chatRoomId(chatRoom.getId())
                            .otherProfileImg(chatRoom.getSender().getProfile_img())
                            .otherNickName(chatRoom.getSender().getNickname())
                            .modifiedAt(chatRoom.getModifiedAt())
                            .lastChat(lastChat)
                            .isNotice(notice)
                            .build();
                    chatRoomListList.add(chatRoomListBuilder);
                }
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

    public ChatRoomResponseDto.ChatMessageListData roomChatListService(Long userId, Long chatRoomId, LocalDateTime localDateTime) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                ()-> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        User another = new User();

        User me = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        //내가 buyer인지 seller인지 구별하기 위한 코드
        if (Objects.equals(chatRoom.getSender().getId(), userId)){
            another = chatRoom.getReceiver();
        }else {
            another = chatRoom.getSender();
        }

        LocalDateTime lastDateTime = null;
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomIdOrderByModifiedAt(chatRoomId);

        int resultCount = chatMessageList.size();

        if(resultCount>=1){
            lastDateTime = chatMessageList.get(resultCount-1).getModifiedAt();
        }

        List<ChatRoomResponseDto.ChatMessageData> chatMessageDataList = new ArrayList<>();
        for( ChatMessage chatMessage : chatMessageList){
            chatMessageDataList.add(ChatRoomResponseDto.ChatMessageData.builder()
                    .message(chatMessage.getMessage())
                    .userNickname(chatMessage.getWriter().getNickname())
                    .messageModifiedDate(chatMessage.getMessageModifiedDate())
                    .messageModifiedTime(chatMessage.getMessageModifiedTime())
                    .build());

        }
//        Notice notice = noticeRepository.findByUserIdAndChatRoomId(userId,chatRoomId).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
//        noticeRepository.deleteById(notice.getId());
//        System.out.println("해당 채팅방 알림 삭제");
        return ChatRoomResponseDto.ChatMessageListData.builder()
                .result("success")
                .msg("해당 채팅방 채팅 내용 반환 성공")
                .resultCount(resultCount)
                .lastDatetime(lastDateTime)
                .myProfileImg(me.getProfile_img())
                .myNickname(me.getNickname())
                .otherProfileImg(another.getProfile_img())
                .otherNickName(another.getNickname())
                .chatMessageDataList(chatMessageDataList)
                .build();

    }
}

