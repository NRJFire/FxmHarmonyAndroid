package com.sofac.fxmharmony.dto;

/**
 * Created by Maxim on 29.11.2017.
 */

public class SenderContainerDTO {

    private String status;
    private Long user_id;
    private String google_key;

    /**
     * constructor for sending "getListTosses"
     */
    public SenderContainerDTO(String status, Long user_id) {
        this.status = status;
        this.user_id = user_id;
    }

    /**
     * constructor for sending "getListPush"
     */
    public SenderContainerDTO(Long user_id, String google_key) {
        this.user_id = user_id;
        this.google_key = google_key;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "SenderContainerDTO{" +
                "status='" + status + '\'' +
                ", user_id=" + user_id +
                ", google_key='" + google_key + '\'' +
                '}';
    }
}
