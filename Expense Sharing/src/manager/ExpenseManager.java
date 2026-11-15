package manager;

import model.*;
import observer.NotificationService;
import repository.ExpenseRepository;
import repository.GroupRepository;
import repository.UserRepository;
import strategy.EqualSplitStrategy;
import strategy.ExactSplitStrategy;
import strategy.PercentageSplitStrategy;
import strategy.SplitStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExpenseManager {
    private static ExpenseManager instance;

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private ExpenseRepository expenseRepository;
    private BalanceSheet balanceSheet;
    private NotificationService notificationService;
    private Map<SplitType, SplitStrategy> strategyMap;

    private ExpenseManager() {
        this.userRepository = new UserRepository();
        this.groupRepository = new GroupRepository();
        this.expenseRepository = new ExpenseRepository();
        this.balanceSheet = new BalanceSheet();
        this.notificationService = new NotificationService();

        this.strategyMap = new HashMap<>();
        this.strategyMap.put(SplitType.EQUAL, new EqualSplitStrategy());
        this.strategyMap.put(SplitType.EXACT, new ExactSplitStrategy());
        this.strategyMap.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
    }

    public static synchronized ExpenseManager getInstance() {
        if (instance == null) {
            instance = new ExpenseManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    public Group getGroup(String groupId) {
        return groupRepository.findById(groupId);
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    /**
     * Core method to add an expense with single/multi payers and different split strategies.
     */
    public Expense addExpense(String description, BigDecimal totalAmount, Group group, 
                              User createdBy, SplitType splitType, 
                              List<Split> paidBy, List<Split> splitAmong) {
        // 1. Validate total amount
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }

        // 2. Validate payers (paidBy)
        BigDecimal totalPaid = BigDecimal.ZERO;
        for (Split p : paidBy) {
            totalPaid = totalPaid.add(p.getAmount());
        }
        if (totalPaid.compareTo(totalAmount) != 0) {
            throw new IllegalArgumentException("Sum of paid amounts (" + totalPaid + ") does not equal total amount (" + totalAmount + ")");
        }

        // 3. Validate split distribution using Strategy
        SplitStrategy strategy = strategyMap.get(splitType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported split strategy: " + splitType);
        }

        if (!strategy.validate(totalAmount, splitAmong)) {
            throw new IllegalArgumentException("Invalid split calculation for strategy " + splitType);
        }

        // 4. Create and persist Expense
        String expenseId = UUID.randomUUID().toString().substring(0, 8);
        Expense expense = new Expense(expenseId, description, totalAmount, group, createdBy, splitType, paidBy, splitAmong);
        expenseRepository.save(expense);

        if (group != null) {
            group.addExpense(expense);
        }

        // 5. Update BalanceSheet
        updateBalancesForExpense(expense);

        // 6. Send notifications via Observer
        notifyParticipants(expense, "New expense added: " + description);

        return expense;
    }

    /**
     * Splits and distributes debt between each payer and each debtor proportionally.
     */
    private void updateBalancesForExpense(Expense expense) {
        BigDecimal totalAmount = expense.getTotalAmount();

        for (Split payerSplit : expense.getPaidBy()) {
            User payer = payerSplit.getUser();
            BigDecimal amountPaid = payerSplit.getAmount();
            BigDecimal payerRatio = amountPaid.divide(totalAmount, 4, RoundingMode.HALF_UP);

            for (Split debtorSplit : expense.getSplitAmong()) {
                User debtor = debtorSplit.getUser();
                BigDecimal debtorShare = debtorSplit.getAmount();

                // How much this debtor owes to this specific payer
                BigDecimal owedToThisPayer = debtorShare.multiply(payerRatio).setScale(2, RoundingMode.HALF_UP);
                balanceSheet.updateBalance(payer, debtor, owedToThisPayer);
            }
        }
    }

    /**
     * Record a direct settle-up payment between two users (FR #3).
     */
    public void recordPayment(User payer, User payee, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        // Payer pays payee: updates balance sheet (payer is owed, payee owes payer, neutralizing debt)
        balanceSheet.updateBalance(payer, payee, amount);

        List<Split> paidBy = List.of(new Split(payer, amount));
        List<Split> splitAmong = List.of(new Split(payee, amount));

        Expense paymentExpense = new Expense(
                UUID.randomUUID().toString().substring(0, 8),
                "Settlement Payment: " + payer.getName() + " -> " + payee.getName(),
                amount,
                null,
                payer,
                SplitType.EXACT,
                paidBy,
                splitAmong
        );
        expenseRepository.save(paymentExpense);

        // Notify both parties
        notificationService.notifyUser(payee, paymentExpense, payer.getName() + " paid you $" + amount);
        notificationService.notifyUser(payer, paymentExpense, "You paid $" + amount + " to " + payee.getName());
    }

    /**
     * View transaction history for a user (FR #4).
     */
    public void showHistory(User user) {
        System.out.println("========== Expense History for " + user.getName() + " ==========");
        List<Expense> history = expenseRepository.getExpensesByUser(user.getUserId());
        if (history.isEmpty()) {
            System.out.println("No expenses found for " + user.getName());
        } else {
            for (Expense exp : history) {
                System.out.println("- [" + exp.getExpenseId() + "] " + exp.getDescription() + 
                                   " | Total: $" + exp.getTotalAmount() + 
                                   " | Type: " + exp.getSplitType() +
                                   (exp.getGroup() != null ? " | Group: " + exp.getGroup().getName() : " | 1-on-1"));
            }
        }
        System.out.println("=================================================");
    }

    /**
     * Observer notification trigger (FR #5).
     */
    private void notifyParticipants(Expense expense, String message) {
        Set<User> notified = new HashSet<>();
        for (Split s : expense.getPaidBy()) {
            if (notified.add(s.getUser())) {
                notificationService.notifyUser(s.getUser(), expense, message);
            }
        }
        for (Split s : expense.getSplitAmong()) {
            if (notified.add(s.getUser())) {
                notificationService.notifyUser(s.getUser(), expense, message);
            }
        }
    }
}

