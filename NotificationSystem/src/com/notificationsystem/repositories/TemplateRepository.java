package com.notificationsystem.repositories;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Template;
import java.util.List;

public interface TemplateRepository {
    void save(Template template);
    Template findById(String templateId);
    List<Template> findByClientIdAndChannel(String clientId, ChannelMedium channel);
    List<Template> listAll();
}
