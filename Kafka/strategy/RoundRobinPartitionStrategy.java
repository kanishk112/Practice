package strategy;

import data.Message;
import data.Partition;
import data.Topic;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinPartitionStrategy implements PartitionStrategy {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Partition getPartition(Topic topic, Message message) {
        if (topic.getPartitions().isEmpty()) return null;
        
        int index = Math.abs(counter.getAndIncrement()) % topic.getPartitions().size();
        return topic.getPartitions().get(index);
    }
}
