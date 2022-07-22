package com.project.hack.chat.repository;

import com.project.hack.chat.model.ChatRoom;
import com.project.hack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByBuyerAndSeller(User buyer, User seller);

    @Query(nativeQuery = true, value = "select * from chat_room cr where cr.buyerid=:userId or cr.sellerid=:userId ORDER BY cr.modified_at desc")
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);

}

