package strategy;

import model.Split;
import model.User;

import java.math.BigDecimal;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy {

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
        // For exact split, amounts are predetermined by the user
        throw new UnsupportedOperationException("Exact splits must provide pre-calculated amounts for each user");
    }
}

