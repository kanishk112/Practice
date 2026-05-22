package atm;

import java.util.Map;
import java.util.Optional;
class ATMRepository {
    private Map<String, ATM> atms;

    public ATMRepository(Map<String, ATM> atms) {
        this.atms = atms;
    }

    public void save(ATM atm) {
        atms.put(atm.getId(), atm);
    }

    public Optional<ATM> getById(String id) {
        return Optional.ofNullable(atms.get(id));
    }

    public void updateATMStatus(String id, ATMStatus newStatus) {
        atms.get(id).setStatus(newStatus);
    }
}