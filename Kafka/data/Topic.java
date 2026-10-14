package data;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<Partition> partitions;

    public Topic(String name) {
        this.name = name;
        this.partitions = new ArrayList<>();
    }

    public void addPartition(Partition partition) {
        this.partitions.add(partition);
    }

    public Partition getPartition(String partitionId) {
        for (Partition p : partitions) {
            if (p.getPartitionId().equals(partitionId)) {
                return p;
            }
        }
        return null;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public String getName() {
        return name;
    }
}
