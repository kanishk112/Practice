package core;

import data.Message;
import data.Partition;

public class Consumer {
    private String consumerId;
    private String groupId;
    private ConsumerGroup consumerGroup;

    public Consumer(String consumerId, ConsumerGroup consumerGroup) {
        this.consumerId = consumerId;
        this.consumerGroup = consumerGroup;
        this.groupId = consumerGroup.getGroupId();
        this.consumerGroup.addConsumer(this);
    }

    public void poll(Broker broker, Partition partition) {
        int currentOffset = consumerGroup.getOffset(partition);
        Message message = broker.fetch(partition, currentOffset);

        if (message != null) {
            System.out.println("Consumer " + consumerId + " (Group: " + groupId + ") fetched: ['" + message.getValue() + "'] from " + partition.getPartitionId());
            consumerGroup.commitOffset(partition, currentOffset + 1);
        } else {
            System.out.println("Consumer " + consumerId + " found no new messages in " + partition.getPartitionId());
        }
    }
}
