package com.sofac.fxmharmony.dto;

import java.io.Serializable;

/**
 * Created by Maxim on 04.12.2017.
 */

public class TossCommentDTO implements Serializable {

    private String id;
    private String body;
    private String date;
    private String user_id;
    private String name;
    private String avatar;

    public TossCommentDTO(String id, String body, String date, String user_id, String name, String avatar) {
        this.id = id;
        this.body = body;
        this.date = date;
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
    }

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

    @Override
    public String toString() {
        return "TossCommentDTO{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
