package data;

public class Message {
    private String key;
    private String value;
    private long timestamp;

    public Message(String key, String value) {
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
    public long getTimestamp() { return timestamp; }
}
