package com.notificationsystem.models;

public class Recipient {
    private String recipientId;
    private String recipientName;
    private String email;
    private String phoneNo;

    public Recipient(String recipientId, String recipientName, String email, String phoneNo) {
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "recipientId='" + recipientId + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
