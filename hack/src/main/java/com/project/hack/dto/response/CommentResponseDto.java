package com.project.hack.dto.response;

import com.project.hack.model.Comment;
import com.project.hack.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long userId;
    private LocalDateTime createdAt;
    private Long postId;
    private Long commentId;
    private String nickname;
    private String name;
    private String comment;
    private String profile_img;

    private String createdAtDateOnly;


    public CommentResponseDto(Comment comment, User user) {
        this.commentId = comment.getCommentId();
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.userId = user.getId();
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.comment = comment.getComment();
        this.name = comment.getUser().getName();
        this.nickname = comment.getUser().getNickname();
        this.profile_img = comment.getUser().getProfile_img();
        this.postId = comment.getPost().getPostId();
        this.createdAt = comment.getCreatedAt();
        this.userId = comment.getUser().getId();
        this.createdAtDateOnly = createAtDateOnly(comment);
    }
        public static String createAtDateOnly(Comment comment){
            LocalDate createdAtDateOnly = comment.getCreatedAtDateOnly();
            String year = Integer.toString(createdAtDateOnly.getYear());
            year = year.substring(year.length()-2,year.length());
            String month = Integer.toString(createdAtDateOnly.getMonthValue());
            String day = Integer.toString(createdAtDateOnly.getDayOfMonth());
            return year+"."+month+"."+day;
        }

        public static String calculatedTime(Comment comment){
            final int SEC = 60;
            final int MIN = 60;
            final int HOUR = 24;
            final int DAY = 30;
            final int MONTH = 12;
            String msg = null;

            LocalDateTime rightNow = LocalDateTime.now();
            LocalDateTime createdAt = comment.getCreatedAt();
            long MILLIS = ChronoUnit.MILLIS.between(createdAt, rightNow);
            long calculate = MILLIS / 1000;

            if (calculate < SEC) {
                msg = calculate + "초 전";
            } else if ((calculate /= SEC) < MIN) {
                msg = calculate + "분 전";
            } else if ((calculate /= MIN) < HOUR) {
                msg = (calculate) + "시간 전";
            } else if ((calculate /= HOUR) < DAY) {
                msg = (calculate) + "일 전";
            } else if ((calculate /= DAY) < MONTH) {
                msg = (calculate) + "개월 전";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String curYear = sdf.format(rightNow);
                String passYear = sdf.format(createdAt);
                int diffYear = Integer.parseInt(curYear) - Integer.parseInt(passYear);
                msg = diffYear + "년 전";
            }
            return msg;
        }
    }
