package strategy;

import model.Split;
import model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements SplitStrategy {

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
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participants list cannot be empty");
        }

        int count = participants.size();
        BigDecimal share = totalAmount.divide(BigDecimal.valueOf(count), 2, RoundingMode.DOWN);
        BigDecimal remainder = totalAmount.subtract(share.multiply(BigDecimal.valueOf(count)));

        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BigDecimal splitAmount = share;
            // Add remaining cents to the first participant to maintain exact sum
            if (i == 0) {
                splitAmount = splitAmount.add(remainder);
            }
            splits.add(new Split(participants.get(i), splitAmount));
        }
        return splits;
    }
}

