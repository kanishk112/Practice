package Model;

import java.util.Date;
import startegy.TaskState;

public class Task {
    private final String taskId;
    private final String taskTitle;
    private final String taskDescription;
    private final Date createdAt;
    private final Date dueDate;
    private User assignedTo;
    private TaskState state;

    public  Task(String taskId, String taskTitle, String taskDescription, Date createdAt, Date dueDate, Date dueDate1) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.createdAt = createdAt;
        this.dueDate = dueDate1;
    }

    public String getTaskId() {
        return taskId;
    }

    public User getAssignedTo() {
        return assignedTo;
    }
}
