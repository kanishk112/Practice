package com.notificationsystem.models;

public class Client {
    private String clientId;
    private String clientName;
    private String email;
    private String description;

    public Client(String clientId, String clientName, String email, String description) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.email = email;
        this.description = description;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
