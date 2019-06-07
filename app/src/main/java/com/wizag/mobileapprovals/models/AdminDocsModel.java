package com.wizag.mobileapprovals.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AdminDocsModel {
    @SerializedName("success")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("documents")
    public List<AdminDocuments> adminDocumentsList = new ArrayList<>();


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AdminDocuments> getAdminDocumentsList() {
        return adminDocumentsList;
    }

    public void setAdminDocumentsList(List<AdminDocuments> adminDocumentsList) {
        this.adminDocumentsList = adminDocumentsList;
    }
}
