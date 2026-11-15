package strategy;

import model.Split;
import model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public boolean validate(BigDecimal totalAmount, List<Split> splits) {
        if (splits == null || splits.isEmpty()) return false;
        BigDecimal sum = BigDecimal.ZERO;
        for (Split s : splits) {
            sum = sum.add(s.getAmount());
        }
        return sum.compareTo(totalAmount) == 0;
    }

    @Override
    public List<Split> calculateSplits(BigDecimal totalAmount, List<User> participants) {
        throw new UnsupportedOperationException("Percentage splits require percentage proportions per user");
    }

    // Helper for percentage based calculation
    public List<Split> calculatePercentageSplits(BigDecimal totalAmount, Map<User, Double> percentages) {
        double totalPercent = 0.0;
        for (double p : percentages.values()) {
            totalPercent += p;
        }
        if (Math.abs(totalPercent - 100.0) > 0.001) {
            throw new IllegalArgumentException("Percentages must add up to 100%");
        }

        List<Split> splits = new ArrayList<>();
        BigDecimal allocated = BigDecimal.ZERO;
        List<User> userList = new ArrayList<>(percentages.keySet());

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            double pct = percentages.get(user);

            if (i == userList.size() - 1) {
                // Assign remainder to avoid rounding mismatch
                splits.add(new Split(user, totalAmount.subtract(allocated)));
            } else {
                BigDecimal amount = totalAmount.multiply(BigDecimal.valueOf(pct))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                allocated = allocated.add(amount);
                splits.add(new Split(user, amount));
            }
        }
        return splits;
    }
}

