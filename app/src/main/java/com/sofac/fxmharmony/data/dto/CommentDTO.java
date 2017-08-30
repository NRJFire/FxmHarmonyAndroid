package com.sofac.fxmharmony.data.dto;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class CommentDTO extends SugarRecord implements Serializable {

    public CommentDTO() {
    }

    public CommentDTO(Long id, Long serverID, Long userID, String userName, Date date, String commentText, Long postID,  String commentUserAvatarImage) {
        this.id = id;
        this.serverID = serverID;
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.commentText = commentText;
        this.postID = postID;
        this.commentUserAvatarImage = commentUserAvatarImage;
    }

    private transient Long id;
    private Long serverID;
    private Long userID;
    private String userName;
    private Date date;
    private String commentText;
    private Long postID;
    private String commentUserAvatarImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    public String getCommentUserAvatarImage() {
        return commentUserAvatarImage;
    }

    public void setCommentUserAvatarImage(String commentUserAvatarImage) {
        this.commentUserAvatarImage = commentUserAvatarImage;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", serverID=" + serverID +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", commentText='" + commentText + '\'' +
                ", postID=" + postID +
                ", commentUserAvatarImage='" + commentUserAvatarImage + '\'' +
                '}';
    }
}
