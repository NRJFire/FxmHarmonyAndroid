package com.sofac.fxmharmony.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxim on 29.11.2017.
 */

public class SenderContainerDTO {

    private String status;
    private Long user_id;
    private String google_key;
    private String toss_message_id;
    private String body;
    private String toss_id;
    private String title;
    private String date_end;
    private HashMap<String,Integer> managers;


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

    /**
     * constructor for sending "addTossComment"
     */
    public SenderContainerDTO(Long user_id, String toss_message_id, String body, String toss_id) {
        this.user_id = user_id;
        this.toss_message_id = toss_message_id;
        this.body = body;
        this.toss_id = toss_id;
    }

    /**
     * constructor for sending "addToss"
     */
    public SenderContainerDTO(Long user_id, String title, String date_end, HashMap<String,Integer> managers, String body) {
        this.user_id = user_id;
        this.title = title;
        this.date_end = date_end;
        this.managers = managers;
        this.body = body;
    }

    // addToss {"dataTransferObject":{"user_id":"12", "title":"test title", "date_end":"2017-12-07 13:14:15", "managers":{"0":11,"1":103}, "body":"test toss from postman"}, "requestType":"addToss"}

    public String getToss_message_id() {
        return toss_message_id;
    }

    public void setToss_message_id(String toss_message_id) {
        this.toss_message_id = toss_message_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToss_id() {
        return toss_id;
    }

    public void setToss_id(String toss_id) {
        this.toss_id = toss_id;
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
                ", toss_message_id='" + toss_message_id + '\'' +
                ", body='" + body + '\'' +
                ", toss_id='" + toss_id + '\'' +
                '}';
    }
}
