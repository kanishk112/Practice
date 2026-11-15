package observer;

import model.Expense;
import model.User;

public interface NotificationObserver {
    void notifyUser(User user, Expense expense, String description);
}

