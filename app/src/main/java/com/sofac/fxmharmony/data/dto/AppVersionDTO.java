package com.sofac.fxmharmony.data.dto;

import java.util.Date;

/**
 * Created by Maxim on 17.08.2017.
 */

public class AppVersionDTO {
    private Long id;
    private Long serverID;
    private String title;
    private String description;
    private Date date;
    private Boolean isImportant;
    private String versionName;
    private Integer versionCode;

    public AppVersionDTO(Long id, Long serverID, String title, String description, Date date, Boolean isImportant, String versionName, Integer versionCode) {
        this.id = id;
        this.serverID = serverID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isImportant = isImportant;
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "AppVersionDTO{" +
                "id=" + id +
                ", serverID=" + serverID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", isImportant=" + isImportant +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
