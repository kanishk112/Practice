package strategy;

import data.Message;
import data.Partition;
import data.Topic;

public class HashPartitionStrategy implements PartitionStrategy {
    @Override
    public Partition getPartition(Topic topic, Message message) {
        if (topic.getPartitions().isEmpty()) return null;
        
        int hashCode = message.getKey() != null ? message.getKey().hashCode() : 0;
        int index = Math.abs(hashCode) % topic.getPartitions().size();
        return topic.getPartitions().get(index);
    }
}
