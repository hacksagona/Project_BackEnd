package com.project.hack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String name;

    @Column
    private String profilImg;

    @Column (nullable = false)
    private String comment;

    /*
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post; */

}
