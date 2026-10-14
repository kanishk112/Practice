package service;

import Startegy.AssignmentStrategy;
import enums.IssueStatus;
import model.Agent;
import model.Issue;
import repository.AgentRepository;
import repository.IssueRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AssignmentService {
    private IssueRepository issueRepository;
    private AgentRepository agentRepository;
    private AssignmentStrategy assignmentStrategy;

    public AssignmentService(IssueRepository issueRepository, AgentRepository agentRepository,
            AssignmentStrategy assignmentStrategy) {
        this.issueRepository = issueRepository;
        this.agentRepository = agentRepository;
        this.assignmentStrategy = assignmentStrategy;
    }

    public void assignIssue(String issueId) {
        Issue issue = issueRepository.getById(issueId);
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
            Agent bestAgentToWaitlist = agents.stream()
                    .filter(a -> a.getExpertise().contains(issue.getIssueType()))
                    .min(Comparator.comparingInt(a -> a.getWaitList().size())) // Find shortest waitlist
                    .orElse(null);
            if (bestAgentToWaitlist != null) {
                bestAgentToWaitlist.getWaitList().add(issue.getId());
                issue.setStatus(IssueStatus.WAITING);
                System.out.println(">>> No agent found with expertise for issue " + issueId);
            }
        }
    }
}
