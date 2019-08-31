package id.web.michsan.hellotransfer.query;

import id.web.michsan.hellotransfer.model.Account;
import id.web.michsan.hellotransfer.repo.AccountRepository;
import lombok.Setter;

import javax.inject.Inject;

public class AccountQueryHandler {
    @Setter
    @Inject
    private AccountRepository accountRepository;

    public AccountSummaryDisplay showAccountSummary(String accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo);
        return new AccountSummaryDisplay(account.getNumber(), account.getBalance());
    }
}
