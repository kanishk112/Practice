package service;

import Startegy.AssignmentStrategy;
import enums.IssueStatus;
import model.Agent;
import model.Issue;
import repository.AgentRepository;
import repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;

public class AssignmentService {
    private IssueRepository issueRepository;
    private AgentRepository agentRepository;
    private AssignmentStrategy assignmentStrategy;

    public void assignIssue(Issue issueId) {
        Issue issue = issueRepository.getById(String.valueOf(issueId));
        if (issue == null) {
            throw new IllegalArgumentException("issue not found");
        }

        List<Agent> agents = new ArrayList<>(agentRepository.getAll());
        Agent assigned = assignmentStrategy.assign(agents, issue);

        if (assigned != null) {
           assigned.setAssignedIssue(issue.getId());
           issue.setAssignedAgent(assigned.getId());
           System.out.println(">>>> issue : " + issueId + " assigned to agent : " + assigned.getId());
        } else {
            for (Agent agent : agents) {
                if (agent.getExpertise().contains(issue.getIssueType()) && agent.isAvailable()) {
                    agent.getWaitList().add(issue.getId());
                    issue.setStatus(IssueStatus.WAITING);
                    System.out.println(">>>> issue : " + issueId + " assigned to agent : " + agent.getId());
                    return;
                }
            }
            System.out.println(">>> No agent found with expertise for issue " + issueId);
        }
    }
}
