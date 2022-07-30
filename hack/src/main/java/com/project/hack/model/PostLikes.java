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
public class PostLikes extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POSTID")
    private Post post;

    @Column(nullable = false)
    private Long userId;

    public PostLikes(Post post, User user){
        this.post = post;
        this.userId = user.getId();
    }

}
