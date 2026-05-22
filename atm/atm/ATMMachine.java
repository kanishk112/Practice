package atm;

import atm.state.ATMState;
import atm.ATMStatus;

public class ATMMachine {
    private final ATM atm;
    private ATMState state;
    private final ATMRepository atmRepository;
    private Card currentCard;

    public ATMMachine(String atmId, ATMRepository atmRepository) {
        this.atm = atmRepository.getById(atmId).orElseThrow(() -> new IllegalArgumentException("ATM not found"));
        this.atmRepository = atmRepository;
        this.state = ATMStateFactory.getState(atm.getStatus(), this);
    }

    public void insertCard(Card card) {
        state.insertCard(card);
    }

    public void ejectCard() {
        state.ejectCard();
    }

    public void enterPin(int pin) {
        state.enterPin(pin);
    }

    public void selectOption(String optionType) {
        state.selectOption(optionType);
    }

    public void dispenseCash(double amount) {
        state.dispenseCash(amount);
    }

    public void setState(ATMState newState) {
        this.state = newState;
    }

}
