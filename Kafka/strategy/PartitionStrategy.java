package strategy;

import data.Message;
import data.Partition;
import data.Topic;

public interface PartitionStrategy {
    Partition getPartition(Topic topic, Message message);
}
