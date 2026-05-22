package state;
import atm.ATMMachine;
import atm.Card;
import atm.ATMStatus;

public class IdleState {
    private final ATMMachine atmMachine;

    public IdleState(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(Card card) {
        // Transition to CardInsertedState
        atmMachine.setCurrentCard(card);
        System.err.println("Card inserted: " + card.getCardNumber());
        atmMachine.setState(new CardInsertedState(atmMachine));
    }

    @Override
    public void enterPin(int pin) {
        System.err.println("No card inserted. Please insert a card first.");
    }

    @Override
    public void selectOption(String optionType) {
        System.err.println("No card inserted. Please insert a card first.");
    }

    @Override
    public void dispenseCash(double amount) {
        System.err.println("No card inserted. Please insert a card first.");
    }

    @Override
    public void ejectCard() {
        System.err.println("No card inserted. Please insert a card first.");
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.IDLE;
    }
}
