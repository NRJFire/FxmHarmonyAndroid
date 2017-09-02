package com.sofac.fxmharmony.dto;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Maxim on 11.05.2017.
 */

public class PushMessage extends SugarRecord implements Serializable {
        private String title;
        private String message;
        private String date;

        public PushMessage(){}

        public PushMessage(String title, String message, String date) {
            this.title = title;
            this.message = message;
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() { return title; }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "PushMessage{" +
                    "title='" + title + '\'' +
                    ", message='" + message + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
}
