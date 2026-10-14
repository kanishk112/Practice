package Startegy;

import model.Agent;
import model.Issue;

import java.util.List;

public class AvailabilityBasedAssignmentStrategy implements AssignmentStrategy {

    @Override
    public Agent assign(List<Agent> agents, Issue issue) {
        if (agents == null || issue == null) {
            return null;
        }

        // Simply returns the first available agent, regardless of their specific expertise
        return agents.stream()
                .filter(Agent::isAvailable)
                .findFirst()
                .orElse(null);
    }
}
