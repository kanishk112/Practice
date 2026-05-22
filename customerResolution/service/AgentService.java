package service;

import enums.IssueType;
import model.Agent;
import repository.AgentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AgentService {
    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public void addAgent(String email, String name, List<IssueType> expertiseAreas) {
        String id = "A" + UUID.randomUUID().toString().substring(0, 6);
        Agent agent = new Agent(id, email, name, new HashSet<>(expertiseAreas));
        agentRepository.save(agent);
        System.out.println(">>> Agent " + id + " created");
    }
}