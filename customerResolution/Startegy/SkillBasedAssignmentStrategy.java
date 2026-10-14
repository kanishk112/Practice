package Startegy;

import model.Agent;
import model.Issue;

import java.util.List;

public class SkillBasedAssignmentStrategy implements AssignmentStrategy {

    @Override
    public Agent assign(List<Agent> agents, Issue issue) {
        if (agents == null || issue == null) {
            return null;
        }

        // 1. Filter for agents that are available AND have the required expertise
        // 2. If there are multiple, you could sort by least amount of history to distribute load. 
        // For now, we return the first available matching agent.
        return agents.stream()
                .filter(Agent::isAvailable)
                .filter(agent -> agent.getExpertise().contains(issue.getIssueType()))
                .findFirst()
                .orElse(null);
    }
}
