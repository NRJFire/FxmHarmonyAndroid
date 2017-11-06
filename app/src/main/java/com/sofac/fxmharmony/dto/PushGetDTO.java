package com.sofac.fxmharmony.dto;

/**
 * Created by Maxim on 06.11.2017.
 */

public class PushGetDTO {
    Long user_id;
    String google_key;

    public PushGetDTO(Long user_id, String google_key) {
        this.user_id = user_id;
        this.google_key = google_key;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getGoogle_key() {
        return google_key;
    }

    public void setGoogle_key(String google_key) {
        this.google_key = google_key;
    }

    @Override
    public String toString() {
        return "PushGetDTO{" +
                "user_id='" + user_id + '\'' +
                ", google_key='" + google_key + '\'' +
                '}';
    }
}
