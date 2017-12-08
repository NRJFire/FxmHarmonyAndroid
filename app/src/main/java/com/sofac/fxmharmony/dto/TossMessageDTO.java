package com.sofac.fxmharmony.dto;

import android.text.TextUtils;

import com.sofac.fxmharmony.util.TypeFiles;

import java.io.Serializable;
import java.util.ArrayList;

import static com.sofac.fxmharmony.Constants.SPLIT_FILES;

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
    private ArrayList<FileDTO> files;

    public TossMessageDTO(String id, String body, String status, String date, String user_id, String name, String avatar, ArrayList<TossCommentDTO> comments, ArrayList<FileDTO> files) {
        this.id = id;
        this.body = body;
        this.status = status;
        this.date = date;
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
        this.comments = comments;
        this.files = files;
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

    public ArrayList<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileDTO> files) {
        this.files = files;
    }

    public ArrayList<String> getImages() {
        return new TypeFiles(getFiles()).getImages();
    }

    public void setImages() {
    }

    public ArrayList<String> getMovies() {
        return new TypeFiles(getFiles()).getMovies();
    }

    public void setMovies() {
    }

    public ArrayList<String> getDocs() {
        return new TypeFiles(getFiles()).getDocs();
    }

    public void setDocs() {
    }

    @Override
    public String toString() {
        return "TossMessageDTO{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", comments=" + comments +
                ", files=" + files +
                '}';
    }
}
