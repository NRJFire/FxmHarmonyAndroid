package com.sofac.fxmharmony.dto;


import java.io.Serializable;

public class AuthorizationDTO implements Serializable {

    public AuthorizationDTO(String login, String password, String googleCloudKey) {
        this.login = login;
        this.password = password;
        this.googleCloudKey = googleCloudKey;
    }

    private String login;
    private String password;
    private String googleCloudKey;


    public String getLogin() {
        return login;
    }

    public void setLogin(String ssoId) {
        this.login = ssoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleCloudKey() {
        return googleCloudKey;
    }

    public void setGoogleCloudKey(String googleCloudKey) {
        googleCloudKey = googleCloudKey;
    }


    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", googleCloudKey='" + googleCloudKey + '\'' +
                '}';
    }
}
