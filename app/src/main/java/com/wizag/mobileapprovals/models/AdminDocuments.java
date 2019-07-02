package com.wizag.mobileapprovals.models;

 public class AdminDocuments {
    public String DocType;
    public String AppStatus;
    public String DocName;
    public String AccountName;
    public String DocDate;
    public String ExclAmt;
    public String VATAmt;
    public String InclAmt;
    public String StatusDescription;


    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    public String getAppStatus() {
        return AppStatus;
    }

    public void setAppStatus(String appStatus) {
        AppStatus = appStatus;
    }

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
    }

    public String getExclAmt() {
        return ExclAmt;
    }

    public void setExclAmt(String exclAmt) {
        ExclAmt = exclAmt;
    }

    public String getVATAmt() {
        return VATAmt;
    }

    public void setVATAmt(String VATAmt) {
        this.VATAmt = VATAmt;
    }

    public String getInclAmt() {
        return InclAmt;
    }

    public void setInclAmt(String inclAmt) {
        InclAmt = inclAmt;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }


 }
