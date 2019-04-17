package com.wizag.mobileapprovals.models;


public class ApprovalModel {


    String groupID;
    String groupName;

    public ApprovalModel() {
    }

    public ApprovalModel(String groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
    }
    public ApprovalModel(ApprovalModel approvalModel) {
        this.groupID = approvalModel.groupID;
        this.groupName = approvalModel.groupName;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}