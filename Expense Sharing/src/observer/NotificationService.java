package observer;

import model.Expense;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements NotificationObserver {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyUser(User user, Expense expense, String description) {
        // Default console notification dispatch
        System.out.println("[NOTIFICATION] To: " + user.getName() + " (" + user.getEmail() + ") | " + description);
        if (expense != null) {
            System.out.println("               Expense: " + expense.getDescription() + " (Amount: $" + expense.getTotalAmount() + ")");
        }

        // Notify downstream channel observers if any
        for (NotificationObserver obs : observers) {
            obs.notifyUser(user, expense, description);
        }
    }
}

