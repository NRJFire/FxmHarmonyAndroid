package com.sofac.fxmharmony.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Maxim on 29.11.2017.
 */

public class TossDTO implements Serializable {

    private String id;
    private String user_id;
    private String title;
    private String date;
    private String date_end;
    private String now;
    private String name;
    private String status;
    private ResponsibleUserDTO[] responsible;
    private ArrayList<TossMessageDTO> messages;

    public TossDTO(String id, String user_id, String title, String date, String date_end, String now, String name, String status, ResponsibleUserDTO[] responsible, ArrayList<TossMessageDTO> messages) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.date = date;
        this.date_end = date_end;
        this.now = now;
        this.name = name;
        this.status = status;
        this.responsible = responsible;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponsibleUserDTO[] getResponsible() {
        return responsible;
    }

    public void setResponsible(ResponsibleUserDTO[] responsible) {
        this.responsible = responsible;
    }

    public ArrayList<TossMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<TossMessageDTO> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "TossDTO{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", date_end='" + date_end + '\'' +
                ", now='" + now + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", responsible=" + Arrays.toString(responsible) +
                ", messages=" + messages +
                '}';
    }
}
