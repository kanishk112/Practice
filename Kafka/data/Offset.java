package data;

public class Offset {
    private String partitionId;
    private int position;

    public Offset(String partitionId, int position) {
        this.partitionId = partitionId;
        this.position = position;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
