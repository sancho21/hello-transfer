package id.web.michsan.hellotransfer.query;

import id.web.michsan.hellotransfer.repo.AccountRepository;
import lombok.Setter;

import java.math.BigDecimal;

public class AccountQueryHandler {
    @Setter
    private AccountRepository accountRepository;

    public BalanceDisplay showBalance(String accountNo) {
        BigDecimal balance = accountRepository.getBalanceByAccountNumber(accountNo);
        return new BalanceDisplay(accountNo, balance);
    }
}
