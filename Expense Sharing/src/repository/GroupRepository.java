package repository;

import model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupRepository {
    private Map<String, Group> groupMap = new ConcurrentHashMap<>();

    public void save(Group group) {
        groupMap.put(group.getGroupId(), group);
    }

    public Group findById(String groupId) {
        return groupMap.get(groupId);
    }

    public List<Group> listAllGroups() {
        return new ArrayList<>(groupMap.values());
    }
}

