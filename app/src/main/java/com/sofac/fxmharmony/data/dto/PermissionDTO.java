package com.sofac.fxmharmony.data.dto;
import com.orm.SugarRecord;

import java.io.Serializable;

public class PermissionDTO extends SugarRecord implements Serializable {

    public PermissionDTO(){}

    public PermissionDTO(Boolean superAdminPermission, Boolean estimatePermission, Boolean casePermission, Boolean customerPermission, Boolean projectsPermission, Boolean staffPermission, Boolean noticePermission, Boolean permissionManagement, Boolean translatePermission, Boolean isLeaderGroup, Boolean isMemberGroup, Boolean isStaffGroup) {
        this.superAdminPermission = superAdminPermission;
        this.estimatePermission = estimatePermission;
        this.casePermission = casePermission;
        this.customerPermission = customerPermission;
        this.projectsPermission = projectsPermission;
        this.staffPermission = staffPermission;
        this.noticePermission = noticePermission;
        this.permissionManagement = permissionManagement;
        this.translatePermission = translatePermission;
        this.isLeaderGroup = isLeaderGroup;
        this.isMemberGroup = isMemberGroup;
        this.isStaffGroup = isStaffGroup;
    }

    private Boolean superAdminPermission;

    private Boolean estimatePermission;

    private Boolean casePermission;

    private Boolean customerPermission;

    private Boolean projectsPermission;

    private Boolean staffPermission;

    private Boolean noticePermission;

    private Boolean permissionManagement;

    private Boolean translatePermission;

    private Boolean isLeaderGroup;

    private Boolean isMemberGroup;

    private Boolean isStaffGroup;

    public Boolean getSuperAdminPermission() {
        return superAdminPermission;
    }

    public void setSuperAdminPermission(Boolean superAdminPermission) {
        this.superAdminPermission = superAdminPermission;
    }

    public Boolean getEstimatePermission() {
        return estimatePermission;
    }

    public void setEstimatePermission(Boolean estimatePermission) {
        this.estimatePermission = estimatePermission;
    }

    public Boolean getCasePermission() {
        return casePermission;
    }

    public void setCasePermission(Boolean casePermission) {
        this.casePermission = casePermission;
    }

    public Boolean getCustomerPermission() {
        return customerPermission;
    }

    public void setCustomerPermission(Boolean customerPermission) {
        this.customerPermission = customerPermission;
    }

    public Boolean getProjectsPermission() {
        return projectsPermission;
    }

    public void setProjectsPermission(Boolean projectsPermission) {
        this.projectsPermission = projectsPermission;
    }

    public Boolean getStaffPermission() {
        return staffPermission;
    }

    public void setStaffPermission(Boolean staffPermission) {
        this.staffPermission = staffPermission;
    }

    public Boolean getNoticePermission() {
        return noticePermission;
    }

    public void setNoticePermission(Boolean noticePermission) {
        this.noticePermission = noticePermission;
    }

    public Boolean getPermissionManagement() {
        return permissionManagement;
    }

    public void setPermissionManagement(Boolean permissionManagement) {
        this.permissionManagement = permissionManagement;
    }

    public Boolean getTranslatePermission() {
        return translatePermission;
    }

    public void setTranslatePermission(Boolean translatePermission) {
        this.translatePermission = translatePermission;
    }

    public Boolean getLeaderGroup() {
        return isLeaderGroup;
    }

    public void setLeaderGroup(Boolean leaderGroup) {
        isLeaderGroup = leaderGroup;
    }

    public Boolean getMemberGroup() {
        return isMemberGroup;
    }

    public void setMemberGroup(Boolean memberGroup) {
        isMemberGroup = memberGroup;
    }

    public Boolean getStaffGroup() {
        return isStaffGroup;
    }

    public void setStaffGroup(Boolean staffGroup) {
        isStaffGroup = staffGroup;
    }

    @Override
    public String toString() {
        return "PermissionDTO{" +
                "superAdminPermission=" + superAdminPermission +
                ", estimatePermission=" + estimatePermission +
                ", casePermission=" + casePermission +
                ", customerPermission=" + customerPermission +
                ", projectsPermission=" + projectsPermission +
                ", staffPermission=" + staffPermission +
                ", noticePermission=" + noticePermission +
                ", permissionManagement=" + permissionManagement +
                ", translatePermission=" + translatePermission +
                ", isLeaderGroup=" + isLeaderGroup +
                ", isMemberGroup=" + isMemberGroup +
                ", isStaffGroup=" + isStaffGroup +
                '}';
    }
}
