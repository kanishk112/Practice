package repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import model.Agent;

public class AgentRepository {
    // This class would contain methods to interact with the data source for agents
    private final ConcurrentHashMap<String, Agent> agents = new ConcurrentHashMap<>();

    public void save(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Agent getById(String id) {
        return agents.get(id);

    }

    public Collection<Agent> getAll() {
        return agents.values();
    }
}