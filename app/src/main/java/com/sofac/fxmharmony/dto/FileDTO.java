package com.sofac.fxmharmony.dto;

/**
 * Created by Maxim on 08.12.2017.
 */

public class FileDTO {
    private String id;
    private String item_id;
    private String user_id;
    private String name;
    private String type;

    public FileDTO(String id, String item_id, String user_id, String name, String type) {
        this.id = id;
        this.item_id = item_id;
        this.user_id = user_id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id='" + id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
