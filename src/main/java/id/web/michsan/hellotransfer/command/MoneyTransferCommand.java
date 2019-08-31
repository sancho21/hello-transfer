package id.web.michsan.hellotransfer.command;

import lombok.Getter;

import java.math.BigDecimal;

public class MoneyTransferCommand {
    @Getter
    private String sourceAccountNo;
    @Getter
    private String beneficiaryAccountNo;
    @Getter
    private BigDecimal amount;

    public MoneyTransferCommand(String sourceAccountNo, String beneficiaryAccountNo, BigDecimal amount) {
        this.sourceAccountNo = sourceAccountNo;
        this.beneficiaryAccountNo = beneficiaryAccountNo;
        this.amount = amount;
    }
}
