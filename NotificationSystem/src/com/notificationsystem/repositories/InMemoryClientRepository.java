package com.notificationsystem.repositories;

import com.notificationsystem.models.Client;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryClientRepository implements ClientRepository {
    private final Map<String, Client> store = new ConcurrentHashMap<>();

    @Override
    public List<Client> listAllClients() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Client client) {
        if (client != null && client.getClientId() != null) {
            store.put(client.getClientId(), client);
        }
    }

    @Override
    public Client findById(String clientId) {
        return store.get(clientId);
    }
}
