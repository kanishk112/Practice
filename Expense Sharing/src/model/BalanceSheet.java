package model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheet {
    // balance[U1][U2] = amount:
    // If positive (> 0): U2 owes U1 this amount (U1 is owed)
    // If negative (< 0): U1 owes U2 this amount (U1 owes to pay)
    private Map<User, Map<User, BigDecimal>> balance = new ConcurrentHashMap<>();

    public Map<User, Map<User, BigDecimal>> getBalance() {
        return balance;
    }

    /**
     * Atomically updates balance between payer and debtor.
     * Debtor owes payer the given amount.
     */
    public void updateBalance(User payer, User debtor, BigDecimal amount) {
        if (payer.equals(debtor) || amount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // Ordered locking to ensure thread safety and prevent deadlocks
        User firstLock = payer.getUserId().compareTo(debtor.getUserId()) < 0 ? payer : debtor;
        User secondLock = firstLock.equals(payer) ? debtor : payer;

        synchronized (firstLock) {
            synchronized (secondLock) {
                // Ensure maps exist
                balance.putIfAbsent(payer, new ConcurrentHashMap<>());
                balance.putIfAbsent(debtor, new ConcurrentHashMap<>());

                // Debtor owes payer:
                // From payer's perspective: payer is owed (+)
                BigDecimal currentPayerBalance = balance.get(payer).getOrDefault(debtor, BigDecimal.ZERO);
                balance.get(payer).put(debtor, currentPayerBalance.add(amount));

                // From debtor's perspective: debtor owes (-)
                BigDecimal currentDebtorBalance = balance.get(debtor).getOrDefault(payer, BigDecimal.ZERO);
                balance.get(debtor).put(payer, currentDebtorBalance.subtract(amount));
            }
        }
    }

    /**
     * Total amount other users owe to this user.
     */
    public BigDecimal showOwed(User user) {
        Map<User, BigDecimal> userBalances = balance.getOrDefault(user, Collections.emptyMap());
        BigDecimal totalOwed = BigDecimal.ZERO;
        for (BigDecimal amt : userBalances.values()) {
            if (amt.compareTo(BigDecimal.ZERO) > 0) {
                totalOwed = totalOwed.add(amt);
            }
        }
        return totalOwed;
    }

    /**
     * Total amount this user owes to other users.
     */
    public BigDecimal showToPay(User user) {
        Map<User, BigDecimal> userBalances = balance.getOrDefault(user, Collections.emptyMap());
        BigDecimal totalToPay = BigDecimal.ZERO;
        for (BigDecimal amt : userBalances.values()) {
            if (amt.compareTo(BigDecimal.ZERO) < 0) {
                totalToPay = totalToPay.add(amt.abs());
            }
        }
        return totalToPay;
    }

    /**
     * Shows sum of all expenses this user paid within a group.
     */
    public BigDecimal showAllExpense(User user, Group group) {
        BigDecimal total = BigDecimal.ZERO;
        if (group == null || group.getExpenses() == null) return total;

        for (Expense e : group.getExpenses()) {
            for (Split s : e.getPaidBy()) {
                if (s.getUser().equals(user)) {
                    total = total.add(s.getAmount());
                }
            }
        }
        return total;
    }

    /**
     * Helper to print detailed 1-on-1 balances for a user.
     */
    public void printUserBalance(User user) {
        System.out.println("----- Balance Sheet for " + user.getName() + " -----");
        Map<User, BigDecimal> userBalances = balance.getOrDefault(user, Collections.emptyMap());
        boolean hasBalances = false;

        for (Map.Entry<User, BigDecimal> entry : userBalances.entrySet()) {
            User otherUser = entry.getKey();
            BigDecimal amount = entry.getValue();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(otherUser.getName() + " owes " + user.getName() + ": $" + amount);
                hasBalances = true;
            } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(user.getName() + " owes " + otherUser.getName() + ": $" + amount.abs());
                hasBalances = true;
            }
        }

        if (!hasBalances) {
            System.out.println("All settled up! No outstanding balances.");
        }
        System.out.println("Total Owed to " + user.getName() + ": $" + showOwed(user));
        System.out.println("Total " + user.getName() + " owes: $" + showToPay(user));
        System.out.println("----------------------------------------");
    }
}

