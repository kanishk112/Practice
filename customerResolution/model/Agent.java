package model;

import java.util.Collection;
import java.util.Set;
import enums.IssueType;
import java.util.List;
import java.util.Queue;

public class Agent {
    private String id;
    private String name;
    private String email;
    private Set<IssueType> expertiseAreas;

    private Queue<String> waitList;
    private List<String> assignedIssues;
    private String assignedIssueId;
    private List<String> history;

    public Agent(String id, String name, String email, Set<IssueType> expertiseAreas) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.expertiseAreas = expertiseAreas;
    }

    public boolean isAvailable() {
        return assignedIssueId == null;
    }

    public String getId() {
        return id;
    }

    public void setAssignedIssue(String id) {
        this.assignedIssueId = id;
    }

    public Set<IssueType> getExpertise() {
        return this.expertiseAreas;
    }

    public Queue<String> getWaitList() {
        return waitList;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setAssignedIssueId(String assignedIssueId) {
        this.assignedIssueId = assignedIssueId;
    }
}