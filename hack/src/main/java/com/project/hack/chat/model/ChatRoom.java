package com.project.hack.chat.model;


import com.project.hack.model.Post;
import com.project.hack.model.Timestamped;
import com.project.hack.model.User;
import lombok.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends Timestamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SELLERID")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="BUYERID")
    private User buyer;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public static ChatRoom create(ChatRoom create){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.seller = create.getSeller();
        chatRoom.buyer = create.getBuyer();
        return chatRoom;
    }

}
