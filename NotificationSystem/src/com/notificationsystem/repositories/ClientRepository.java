package com.notificationsystem.repositories;

import com.notificationsystem.models.Client;
import java.util.List;

public interface ClientRepository {
    List<Client> listAllClients();
    void save(Client client);
    Client findById(String clientId);
}
