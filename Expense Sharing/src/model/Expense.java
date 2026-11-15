package model;

import java.math.BigDecimal;
import java.util.List;

public class Expense {
    private String expenseId;
    private String description;
    private BigDecimal totalAmount;
    private Group group;
    private User createdBy;
    private SplitType splitType;
    private List<Split> paidBy;
    private List<Split> splitAmong;

    public Expense(String expenseId, String description, BigDecimal totalAmount, 
                   Group group, User createdBy, SplitType splitType, 
                   List<Split> paidBy, List<Split> splitAmong) {
        this.expenseId = expenseId;
        this.description = description;
        this.totalAmount = totalAmount;
        this.group = group;
        this.createdBy = createdBy;
        this.splitType = splitType;
        this.paidBy = paidBy;
        this.splitAmong = splitAmong;

        // Associate back to expense
        if (paidBy != null) {
            for (Split s : paidBy) s.setExpense(this);
        }
        if (splitAmong != null) {
            for (Split s : splitAmong) s.setExpense(this);
        }
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Group getGroup() {
        return group;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public List<Split> getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplitAmong() {
        return splitAmong;
    }
}

