package com.sofac.fxmharmony.dto;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Maxim on 11.05.2017.
 */

public class PushMessage extends SugarRecord implements Serializable {
        private String title;
        private String body;
        private String date;

    public PushMessage(){}

    public PushMessage(String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "PushMessage{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
