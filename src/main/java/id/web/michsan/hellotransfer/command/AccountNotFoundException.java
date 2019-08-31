package id.web.michsan.hellotransfer.command;

import lombok.Getter;

public class AccountNotFoundException extends RuntimeException {
    @Getter
    private final String accountNumber;

    public AccountNotFoundException(String accountNumber) {
        super("Unknown account: " + accountNumber);
        this.accountNumber = accountNumber;
    }
}
