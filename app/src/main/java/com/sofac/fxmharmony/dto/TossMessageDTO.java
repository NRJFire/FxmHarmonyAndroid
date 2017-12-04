package com.sofac.fxmharmony.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Maxim on 04.12.2017.
 */

public class TossMessageDTO implements Serializable {
    private String id;
    private String body;
    private String status;
    private String date;
    private String user_id;
    private String name;
    private String avatar;
    private ArrayList<TossCommentDTO> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<TossCommentDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<TossCommentDTO> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "\n\nTossMessageDTO{" +
                "\n\tid='" + id + '\'' +
                ", \n\tbody='" + body + '\'' +
                ", \n\tstatus='" + status + '\'' +
                ", \n\tdate='" + date + '\'' +
                ", \n\tuser_id='" + user_id + '\'' +
                ", \n\tname='" + name + '\'' +
                ", \n\tavatar='" + avatar + '\'' +
                ", \n\tcomments=" + comments +
                '}';
    }
}
