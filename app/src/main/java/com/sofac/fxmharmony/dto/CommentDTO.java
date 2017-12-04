package com.sofac.fxmharmony.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentDTO implements Serializable {

    private Long id;
    private Long user_id;
    private Long post_id;
    private String body;
    private String name;
    private String date;
    private String avatar;

    public CommentDTO(Long id, Long user_id, Long post_id, String body, String name, String date, String avatar) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.body = body;
        this.name = name;
        this.date = date;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
        try {
            return  dateParser.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", post_id=" + post_id +
                ", body='" + body + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
