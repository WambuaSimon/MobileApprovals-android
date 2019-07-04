package com.wizag.mobileapprovals.models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("DocId")
    @Expose
    private Integer docId;
    @SerializedName("DocType")
    @Expose
    private Integer docType;
    @SerializedName("SequenceID")
    @Expose
    private List<SequenceID> sequenceID = null;
    @SerializedName("GroupID")
    @Expose
    private Integer groupID;
    @SerializedName("LastGroup")
    @Expose
    private Integer lastGroup;
    @SerializedName("LastAgent")
    @Expose
    private Integer lastAgent;
    @SerializedName("NextGroup")
    @Expose
    private Integer nextGroup;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("document")
    @Expose
    private Document_ document;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getDocType() {
        return docType;
    }

    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    public List<SequenceID> getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(List<SequenceID> sequenceID) {
        this.sequenceID = sequenceID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Object getLastGroup() {
        return lastGroup;
    }

    public void setLastGroup(Integer lastGroup) {
        this.lastGroup = lastGroup;
    }

    public Integer getLastAgent() {
        return lastAgent;
    }

    public void setLastAgent(Integer lastAgent) {
        this.lastAgent = lastAgent;
    }

    public Integer getNextGroup() {
        return nextGroup;
    }

    public void setNextGroup(Integer nextGroup) {
        this.nextGroup = nextGroup;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Document_ getDocument() {
        return document;
    }

    public void setDocument(Document_ document) {
        this.document = document;
    }


    public class Document_ {

        @SerializedName("DocId")
        @Expose
        private Integer docId;
        @SerializedName("DocType")
        @Expose
        private Integer docType;
        @SerializedName("DocName")
        @Expose
        private String docName;
        @SerializedName("AccountName")
        @Expose
        private String accountName;
        @SerializedName("DocDate")
        @Expose
        private String docDate;
        @SerializedName("ExclAmt")
        @Expose
        private Integer exclAmt;
        @SerializedName("VATAmt")
        @Expose
        private Integer vATAmt;
        @SerializedName("InclAmt")
        @Expose
        private Integer inclAmt;
        @SerializedName("Price")
        @Expose
        private Integer price;
        @SerializedName("Quantity")
        @Expose
        private Integer quantity;
        @SerializedName("Total")
        @Expose
        private Integer total;
        @SerializedName("AppStatus")
        @Expose
        private Integer appStatus;
        @SerializedName("RejectionReason")
        @Expose
        private String rejectionReason;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getDocId() {
            return docId;
        }

        public void setDocId(Integer docId) {
            this.docId = docId;
        }

        public Integer getDocType() {
            return docType;
        }

        public void setDocType(Integer docType) {
            this.docType = docType;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getDocDate() {
            return docDate;
        }

        public void setDocDate(String docDate) {
            this.docDate = docDate;
        }

        public Integer getExclAmt() {
            return exclAmt;
        }

        public void setExclAmt(Integer exclAmt) {
            this.exclAmt = exclAmt;
        }

        public Integer getVATAmt() {
            return vATAmt;
        }

        public void setVATAmt(Integer vATAmt) {
            this.vATAmt = vATAmt;
        }

        public Integer getInclAmt() {
            return inclAmt;
        }

        public void setInclAmt(Integer inclAmt) {
            this.inclAmt = inclAmt;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getAppStatus() {
            return appStatus;
        }

        public void setAppStatus(Integer appStatus) {
            this.appStatus = appStatus;
        }

        public String getRejectionReason() {
            return rejectionReason;
        }

        public void setRejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class Example {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("documents")
        @Expose
        private List<UserModel> documents = null;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<UserModel> getDocuments() {
            return documents;
        }

        public void setDocuments(List<UserModel> documents) {
            this.documents = documents;
        }

    }

    public class SequenceID {

        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}