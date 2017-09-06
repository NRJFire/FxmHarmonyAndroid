package com.sofac.fxmharmony.dto;

import com.orm.SugarRecord;
import java.io.Serializable;

/**
 * Created by Maxim on 17.08.2017.
 */

public class AppVersionDTO extends SugarRecord implements Serializable {
    private String title;
    private String body;
    private String date;
    private String important;
    private String version_name;
    private Integer version_code;

    public AppVersionDTO(Long id, String title, String body, String date, String important, String version_name, Integer version_code) {
        setId(id);
        this.title = title;
        this.body = body;
        this.date = date;
        this.important = important;
        this.version_name = version_name;
        this.version_code = version_code;
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

    public Boolean getImportant() {
        return important.equals("1");
    }

    public void setImportant(Boolean important) {
        if(important) this.important = "1";
        else this.important = "0";
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public Integer getVersion_code() {
        return version_code;
    }

    public void setVersion_code(Integer version_code) {
        this.version_code = version_code;
    }

    @Override
    public String toString() {
        return "AppVersionDTO{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", important=" + important +
                ", version_name='" + version_name + '\'' +
                ", version_code=" + version_code +
                '}';
    }
}
