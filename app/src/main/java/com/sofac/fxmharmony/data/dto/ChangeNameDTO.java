package com.sofac.fxmharmony.data.dto;

public class ChangeNameDTO {

    public ChangeNameDTO(){}

    public ChangeNameDTO(Long userID, String newUserName) {
        this.userID = userID;
        this.newUserName = newUserName;
    }

    private Long userID;

    private String newUserName;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }
}
