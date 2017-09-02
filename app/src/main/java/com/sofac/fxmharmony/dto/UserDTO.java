package com.sofac.fxmharmony.dto;
import com.orm.SugarRecord;
import java.io.Serializable;

/**
 * Created by Maxim on 02.09.2017.
 */

public class UserDTO extends SugarRecord implements Serializable {

    private String login;
    private String password;
    private String role;
    private Boolean visible;
    private String access;

    public UserDTO(){}

    public UserDTO(Long id, String login, String password, String role, Boolean visible, String access) {
        setId(id);
        this.login = login;
        this.password = password;
        this.role = role;
        this.visible = visible;
        this.access = access;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAccess() {
        if (access != null) return access;
        else return "";
    }

    public Boolean isAccessEstimations() {
        if (getAccess().contains("estimations")) return true;
        else return false;
    }

    public Boolean isAccessProjects() {
        if (getAccess().contains("projects")) return true;
        else return false;
    }

    public Boolean isAccessCases() {
        if (getAccess().contains("cases")) return true;
        else return false;
    }

    public Boolean isAccessPages() {
        if (getAccess().contains("pages")) return true;
        else return false;
    }

    public Boolean isAccessCustomers() {
        if (getAccess().contains("customers")) return true;
        else return false;
    }

    public Boolean isAccessManagers() {
        if (getAccess().contains("managers")) return true;
        else return false;
    }

    public Boolean isAccessNotices() {
        if (getAccess().contains("notices")) return true;
        else return false;
    }

    public Boolean isAccessDashboard() {
        if (getAccess().contains("dashboard")) return true;
        else return false;
    }

    public Boolean isAccessApplications() {
        if (getAccess().contains("applications")) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id(ORM)='" + getId() + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", visible=" + visible +
                ", access='" + access + '\'' +
                '}';
    }
}



