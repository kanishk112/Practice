package startegy;

import Model.Task;

public interface TaskState {
    default void startProgress(Task task){};
    default void completeTask(Task task){};
    default void reOpenTask(Task task){};
}
