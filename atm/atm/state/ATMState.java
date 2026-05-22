package atm.state;

import atm.ATMStatus;
import atm.Card;

public interface ATMState {
    void insertCard(Card card);
    void enterPin(int pin);
    void selectOption(String optionType);
    void dispenseCash(double amount);
    void ejectCard();
    ATMStatus getStatus();
}
