package atm;

import state.*;

public class ATMStateFactory {
    public static ATMState getState(ATMStatus status, ATMMachine atmMachine) {
        switch (status) {
            case IDLE:
                return new IdleState(atmMachine);
            case CARD_INSERTED:
                return new CardInsertedState(atmMachine);
            case AUTHENTICATED:
                return new AuthenticatedState(atmMachine);
            case DISPENSE_CASH:
                return new DispenseCashState(atmMachine);
            default:
                throw new IllegalArgumentException("Invalid ATM Status");
        }
    }
}