package id.web.michsan.hellotransfer.command;

import lombok.Getter;

import java.math.BigDecimal;

public class CreateAccountCommand {

    @Getter
    private String holderName;
    @Getter
    private final BigDecimal initialBalance;

    public CreateAccountCommand(String holderName, BigDecimal initialBalance) {
        this.holderName = holderName;
        this.initialBalance = initialBalance;
    }
}
