package Startegy;

import model.Agent;
import model.Issue;

import java.util.List;

public interface AssignmentStrategy {
    Agent assign(List<Agent> agents, Issue issue);
}
