package core;

import data.Message;
import data.Partition;
import java.util.HashMap;
import java.util.Map;

public class Broker {
    private String brokerId;
    private Map<String, Partition> partitions;

    public Broker(String brokerId) {
        this.brokerId = brokerId;
        this.partitions = new HashMap<>();
    }

    public void addPartition(Partition partition) {
        this.partitions.put(partition.getPartitionId(), partition);
    }

    public void store(Partition partition, Message message) {
        if (partitions.containsKey(partition.getPartitionId())) {
            partition.append(message);
        }
    }

    public Message fetch(Partition partition, int offset) {
        if (partitions.containsKey(partition.getPartitionId())) {
            return partition.read(offset);
        }
        return null;
    }
}
