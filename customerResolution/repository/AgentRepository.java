package repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Agent;

public class AgentRepository {
    // This class would contain methods to interact with the data source for agents
    private final Map<String, Agent> agents = new HashMap<>();

    public void save(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Agent getById(String id) {
        return agents.get(id);

    }

    public Collection<Agent> getAll() {
        return  agents.values();
    }
}