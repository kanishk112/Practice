package core;

import data.Message;
import data.Partition;
import data.Topic;
import strategy.PartitionStrategy;

public class Producer {
    private PartitionStrategy strategy;
    private Broker broker;

    public Producer(PartitionStrategy strategy, Broker broker) {
        this.strategy = strategy;
        this.broker = broker;
    }

    public void send(Topic topic, Message message) {
        Partition partition = strategy.getPartition(topic, message);
        if (partition != null) {
            broker.store(partition, message);
            System.out.println("Producer sent message ['" + message.getValue() + "'] to " + partition.getPartitionId());
        }
    }
}
