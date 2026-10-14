package core;

import data.Offset;
import data.Partition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerGroup {
    private String groupId;
    private List<Consumer> consumers;
    private Map<String, Offset> offsets;

    public ConsumerGroup(String groupId) {
        this.groupId = groupId;
        this.consumers = new ArrayList<>();
        this.offsets = new HashMap<>();
    }

    public void addConsumer(Consumer consumer) {
        consumers.add(consumer);
    }

    public void removeConsumer(Consumer consumer) {
        consumers.remove(consumer);
    }

    public void commitOffset(Partition partition, int position) {
        offsets.put(partition.getPartitionId(), new Offset(partition.getPartitionId(), position));
    }

    public int getOffset(Partition partition) {
        Offset offset = offsets.get(partition.getPartitionId());
        return offset != null ? offset.getPosition() : 0;
    }

    public String getGroupId() {
        return groupId;
    }
}
