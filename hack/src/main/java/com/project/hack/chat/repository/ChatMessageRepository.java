package com.project.hack.chat.repository;

import com.project.hack.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
//    List<ChatMessage> findByChatRoomOrderByModifiedAt(Long chatRoomId, LocalDateTime localDateTime);
}
