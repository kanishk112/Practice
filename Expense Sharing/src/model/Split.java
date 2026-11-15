package model;

import java.math.BigDecimal;

public class Split {
    private User user;
    private BigDecimal amount;
    private Expense expense;

    public Split(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return user.getName() + ": $" + amount;
    }
}

