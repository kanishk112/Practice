package strategy;

import model.Split;
import model.User;

import java.math.BigDecimal;
import java.util.List;

public interface SplitStrategy {
    boolean validate(BigDecimal totalAmount, List<Split> splits);
    List<Split> calculateSplits(BigDecimal totalAmount, List<User> participants);
}

