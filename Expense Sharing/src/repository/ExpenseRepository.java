package repository;

import model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpenseRepository {
    private Map<String, Expense> expenseMap = new ConcurrentHashMap<>();

    public void save(Expense expense) {
        expenseMap.put(expense.getExpenseId(), expense);
    }

    public Expense findById(String expenseId) {
        return expenseMap.get(expenseId);
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenseMap.values());
    }

    public List<Expense> getExpensesByGroup(String groupId) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenseMap.values()) {
            if (e.getGroup() != null && e.getGroup().getGroupId().equals(groupId)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Expense> getExpensesByUser(String userId) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenseMap.values()) {
            boolean involved = false;
            for (var split : e.getPaidBy()) {
                if (split.getUser().getUserId().equals(userId)) {
                    involved = true;
                    break;
                }
            }
            if (!involved) {
                for (var split : e.getSplitAmong()) {
                    if (split.getUser().getUserId().equals(userId)) {
                        involved = true;
                        break;
                    }
                }
            }
            if (involved) {
                result.add(e);
            }
        }
        return result;
    }
}

