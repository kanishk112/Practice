package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public void save(User user) {
        userMap.put(user.getUserId(), user);
    }

    public User findById(String userId) {
        return userMap.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }
}

