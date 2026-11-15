package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String name;
    private String description;
    private List<User> members;
    private List<Expense> expenses;

    public Group(String groupId, String name, String description) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}

