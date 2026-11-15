import manager.ExpenseManager;
import model.*;
import strategy.EqualSplitStrategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   EXPENSE SHARING APPLICATION (SPLITWISE LLD)   ");
        System.out.println("==================================================\n");

        ExpenseManager manager = ExpenseManager.getInstance();

        // 1. Create Users
        User alice = new User("U1", "Alice", "alice@example.com");
        User bob = new User("U2", "Bob", "bob@example.com");
        User charlie = new User("U3", "Charlie", "charlie@example.com");

        manager.addUser(alice);
        manager.addUser(bob);
        manager.addUser(charlie);

        // 2. Create Group
        Group tripGroup = new Group("G1", "Goa Trip", "Friends trip to Goa");
        tripGroup.addMember(alice);
        tripGroup.addMember(bob);
        tripGroup.addMember(charlie);
        manager.addGroup(tripGroup);

        System.out.println(">>> Scenario 1: Group Expense with EQUAL Split");
        System.out.println("Alice pays $90 for Dinner, split equally between Alice, Bob, and Charlie.\n");

        EqualSplitStrategy equalStrategy = new EqualSplitStrategy();
        List<Split> dinnerPaidBy = List.of(new Split(alice, new BigDecimal("90.00")));
        List<Split> dinnerSplits = equalStrategy.calculateSplits(new BigDecimal("90.00"), List.of(alice, bob, charlie));

        manager.addExpense("Team Dinner", new BigDecimal("90.00"), tripGroup, alice, SplitType.EQUAL, dinnerPaidBy, dinnerSplits);

        System.out.println("\n--- Current Balances ---");
        manager.getBalanceSheet().printUserBalance(alice);
        manager.getBalanceSheet().printUserBalance(bob);

        System.out.println("\n>>> Scenario 2: 1-on-1 Non-Group Expense with EXACT Split");
        System.out.println("Bob pays $50 for Cab: Bob owes $20, Charlie owes $30.\n");

        List<Split> cabPaidBy = List.of(new Split(bob, new BigDecimal("50.00")));
        List<Split> cabSplits = List.of(
                new Split(bob, new BigDecimal("20.00")),
                new Split(charlie, new BigDecimal("30.00"))
        );

        manager.addExpense("Airport Cab", new BigDecimal("50.00"), null, bob, SplitType.EXACT, cabPaidBy, cabSplits);

        System.out.println("\n--- Current Balances ---");
        manager.getBalanceSheet().printUserBalance(bob);
        manager.getBalanceSheet().printUserBalance(charlie);

        System.out.println("\n>>> Scenario 3: Multi-Payer Expense in Group");
        System.out.println("Groceries $100: Alice pays $60, Bob pays $40. Split equally.\n");

        List<Split> groceryPaidBy = List.of(
                new Split(alice, new BigDecimal("60.00")),
                new Split(bob, new BigDecimal("40.00"))
        );
        List<Split> grocerySplits = equalStrategy.calculateSplits(new BigDecimal("100.00"), List.of(alice, bob, charlie));

        manager.addExpense("Groceries", new BigDecimal("100.00"), tripGroup, alice, SplitType.EQUAL, groceryPaidBy, grocerySplits);

        System.out.println("\n--- Balances for Alice, Bob, and Charlie ---");
        manager.getBalanceSheet().printUserBalance(alice);
        manager.getBalanceSheet().printUserBalance(bob);
        manager.getBalanceSheet().printUserBalance(charlie);

        System.out.println("\n>>> Scenario 4: Settle Up (Payment Recording)");
        System.out.println("Bob settles up and pays Alice $30.00.\n");

        manager.recordPayment(bob, alice, new BigDecimal("30.00"));

        System.out.println("\n--- Updated Balances After Settlement ---");
        manager.getBalanceSheet().printUserBalance(alice);
        manager.getBalanceSheet().printUserBalance(bob);

        System.out.println("\n>>> Scenario 5: User History / Audit Trail");
        manager.showHistory(alice);
        manager.showHistory(bob);

        System.out.println("\n=== All Scenarios Executed Successfully ===");
    }
}

