package com.notificationsystem.models;

import java.util.Map;

public class Template {
    private String templateId;
    private String clientId;
    private String contentWithPlaceholders;
    private ChannelMedium channel;
    private TemplateType type;

    public Template(String templateId, String clientId, String contentWithPlaceholders, ChannelMedium channel, TemplateType type) {
        this.templateId = templateId;
        this.clientId = clientId;
        this.contentWithPlaceholders = contentWithPlaceholders;
        this.channel = channel;
        this.type = type;
    }

    public String render(Map<String, String> placeholders) {
        if (contentWithPlaceholders == null) {
            return "";
        }
        String renderedContent = contentWithPlaceholders;
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = "{{" + entry.getKey() + "}}";
                String val = entry.getValue() != null ? entry.getValue() : "";
                renderedContent = renderedContent.replace(key, val);
            }
        }
        return renderedContent;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContentWithPlaceholders() {
        return contentWithPlaceholders;
    }

    public void setContentWithPlaceholders(String contentWithPlaceholders) {
        this.contentWithPlaceholders = contentWithPlaceholders;
    }

    public ChannelMedium getChannel() {
        return channel;
    }

    public void setChannel(ChannelMedium channel) {
        this.channel = channel;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Template{" +
                "templateId='" + templateId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", channel=" + channel +
                ", type=" + type +
                '}';
    }
}
