package model;

import enums.IssueStatus;
import enums.IssueType;

import java.util.spi.ToolProvider;

public class Issue {
    private final String id;
    private final String email;
    private final String transactionId;
    private final IssueType issueType;
    private final String subject;
    private final String description;
    private IssueStatus issueStatus;
    private String resolution;
    private String assignedAgent;

    public Issue(String id, String transactionId, String email, IssueType issueType, String subject, String description) {
        this.id = id;
        this.email = email;
        this.transactionId = transactionId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
    }

    public Issue(String id, String transactionId, IssueType issueType, String subject, String description, String email) {
        this.id = id;
        this.transactionId = transactionId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
        this.email = email;
    }

    public String toString() {
        return id + " {\"" + transactionId + "\", \"" + issueType + "\", \"" + subject + "\", \"" + description + "\", \"" + email + "\", \"" + issueStatus + "\"}";
    }

    public String getId() {
        return id;
    }

    public void setAssignedAgent(String id) {
        this.assignedAgent = id;
    }

    public IssueType getIssueType() {
        return  issueType;
    }

    public void setStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getEmail() {
        return email;
    }

    public IssueStatus getStatus() {
        return  issueStatus;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAssignedAgentId() {
        return  assignedAgent;
    }
}
