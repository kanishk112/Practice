package Repository;

import Model.Task;
import Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();
    private final List<User> users = new ArrayList<>();
    public void save(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public Task findById(String id) {
        return tasks.get(id);
    }

    public List<Task> findAllByUserId(User user) {
        // Hint for using Streams (after you add getAssignedTo in Task)
        return tasks.values().stream()
                .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().equals(user))
                // or if you compare by ID: task.getAssignedTo().getUserId().equals(user.getUserId())
                .collect(Collectors.toList());
    }
}
