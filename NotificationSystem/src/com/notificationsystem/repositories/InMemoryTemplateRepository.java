package com.notificationsystem.repositories;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Template;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTemplateRepository implements TemplateRepository {
    private final Map<String, Template> store = new ConcurrentHashMap<>();

    @Override
    public void save(Template template) {
        if (template != null && template.getTemplateId() != null) {
            store.put(template.getTemplateId(), template);
        }
    }

    @Override
    public Template findById(String templateId) {
        return store.get(templateId);
    }

    @Override
    public List<Template> findByClientIdAndChannel(String clientId, ChannelMedium channel) {
        return store.values().stream()
                .filter(t -> t.getClientId().equalsIgnoreCase(clientId) && 
                             (channel == null || t.getChannel() == channel))
                .collect(Collectors.toList());
    }

    @Override
    public List<Template> listAll() {
        return new ArrayList<>(store.values());
    }
}
