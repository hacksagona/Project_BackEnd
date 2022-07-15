package com.project.hack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PostDontLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dontLikeId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    public PostDontLikes(Long postId, User user){
        this.postId = postId;
        this.userId = user.getId();
    }
}