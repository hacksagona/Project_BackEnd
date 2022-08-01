package com.project.hack.chat.repository;

import com.project.hack.chat.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByUserIdAndChatRoomId(Long userId, Long ChatRoomId);
    List<Notice> findByUserId(Long userId);
}
