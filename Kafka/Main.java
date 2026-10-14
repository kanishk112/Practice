import data.Message;
import data.Partition;
import data.Topic;
import core.Broker;
import core.Producer;
import core.ConsumerGroup;
import core.Consumer;
import strategy.HashPartitionStrategy;

public class Main {
    public static void main(String[] args) {
        // Initialize Broker
        Broker broker = new Broker("Broker-1");

        // Initialize Topic and Partitions
        Topic topic = new Topic("Topic-1");
        Partition p1 = new Partition("Partition-1");
        Partition p2 = new Partition("Partition-2");

        topic.addPartition(p1);
        topic.addPartition(p2);

        broker.addPartition(p1);
        broker.addPartition(p2);

        // Initialize Producer with Hash strategy
        Producer producer = new Producer(new HashPartitionStrategy(), broker);

        // Initialize Consumers and Consumer Group
        ConsumerGroup group = new ConsumerGroup("Group-1");
        Consumer c1 = new Consumer("Consumer-1", group);
        Consumer c2 = new Consumer("Consumer-2", group);

        // Produce Messages
        producer.send(topic, new Message("key1", "Message 1"));
        producer.send(topic, new Message("key2", "Message 2"));
        producer.send(topic, new Message("key3", "Message 3"));

        // Poll Messages
        c1.poll(broker, p1);
        c1.poll(broker, p1);
        c2.poll(broker, p2);
        
        // Polling again where no message might be present
        c1.poll(broker, p1);
        c2.poll(broker, p2);
    }
}
