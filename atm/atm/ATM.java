package atm;

public class ATM {
    private final String id;
    private ATMStatus status;
    private double cashAvailable;
    
    public ATM(String id, double initialCash) {
        this.id = id;
        this.status = ATMStatus.IDLE;
        this.cashAvailable = initialCash;
    }

    public String getId() {
        return id;
    }

    public ATMStatus getStatus() {
        return status;
    }

    public double getCashAvailable() {
        return cashAvailable;
    }

    public void setStatus(ATMStatus status) {
        this.status = status;
    }

    public void setCashAvailable(double cashAvailable) {
        this.cashAvailable = cashAvailable;
    }
}