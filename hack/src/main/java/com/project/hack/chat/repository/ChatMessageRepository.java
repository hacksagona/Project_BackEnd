package com.project.hack.chat.repository;

import com.project.hack.chat.model.ChatMessage;
import com.project.hack.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findByChatRoomIdOrderByModifiedAt(Long chatroomId);
}
