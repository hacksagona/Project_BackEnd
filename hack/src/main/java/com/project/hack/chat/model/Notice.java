package com.project.hack.chat.model;


import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private int count;

    public void updateCount(){
        this.count= count+1;
    }
    public void countToZero(){this.count = 0;}


}
