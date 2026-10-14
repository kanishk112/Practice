package repository;

import model.Issue;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class IssueRepository {
    private final ConcurrentHashMap<String, Issue> issues = new ConcurrentHashMap<>();

    public void save(Issue issue) {
        issues.put(issue.getId(), issue);
    }

    public Issue getById(String id) {
        return issues.get(id);
    }

    public Collection<Issue> getAll() {
        return issues.values();
    }

}
