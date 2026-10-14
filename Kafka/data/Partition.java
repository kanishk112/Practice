package data;

import java.util.ArrayList;
import java.util.List;

public class Partition {
    private String partitionId;
    private List<Message> messages;

    public Partition(String partitionId) {
        this.partitionId = partitionId;
        this.messages = new ArrayList<>();
    }

    public void append(Message message) {
        this.messages.add(message);
    }

    public Message read(int offset) {
        if (offset >= 0 && offset < messages.size()) {
            return messages.get(offset);
        }
        return null;
    }

    public String getPartitionId() {
        return partitionId;
    }
}
