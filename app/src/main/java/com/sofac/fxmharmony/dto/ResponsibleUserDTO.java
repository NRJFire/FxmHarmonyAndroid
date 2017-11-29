package com.sofac.fxmharmony.dto;

/**
 * Created by Maxim on 29.11.2017.
 */

public class ResponsibleUserDTO {

    String id;
    String user_id;
    String name;

    public ResponsibleUserDTO(String id, String user_id, String name) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ResponsibleUserDTO{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}