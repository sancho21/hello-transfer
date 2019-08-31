package id.web.michsan.hellotransfer.command;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(BigDecimal balance, BigDecimal amountToDeduct) {
        super("Unable to deduct " + amountToDeduct + " from the account with balance " + balance);
    }
}
