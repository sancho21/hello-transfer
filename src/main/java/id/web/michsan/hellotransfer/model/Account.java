package id.web.michsan.hellotransfer.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    @Getter
    private String number;
    @Getter
    private String holderName;
    @Getter
    private BigDecimal balance;

    public Account(String holderName, BigDecimal initialBalance) {
        this(UUID.randomUUID().toString(), holderName, initialBalance);
    }

    public Account(String number, String holderName, BigDecimal balance) {
        this.number = number;
        this.holderName = holderName;
        this.balance = balance;
    }
}
