package id.web.michsan.hellotransfer.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
public class AccountSummaryDisplay {
    @Getter
    private String accountNumber;
    @Getter
    private BigDecimal balanceAmount;
}
