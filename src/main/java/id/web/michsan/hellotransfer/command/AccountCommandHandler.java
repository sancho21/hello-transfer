package id.web.michsan.hellotransfer.command;

import id.web.michsan.hellotransfer.repo.AccountRepository;
import id.web.michsan.hellotransfer.repo.IncorrectResultSizeException;
import lombok.Setter;

import java.math.BigDecimal;

public class AccountCommandHandler {
    @Setter
    private AccountRepository accountRepository;

    public void handle(MoneyTransferCommand command) {
        BigDecimal sourceBalance = readAccountBalance(command.getSourceAccountNo());
        BigDecimal beneficiaryBalance = readAccountBalance(command.getBeneficiaryAccountNo());

        boolean sourceBalanceSufficient = sourceBalance.subtract(command.getAmount()).compareTo(BigDecimal.ZERO) >= 0;
        if (sourceBalanceSufficient) {
            accountRepository.updateBalance(command.getSourceAccountNo(), sourceBalance.subtract(command.getAmount()));
            accountRepository.updateBalance(command.getBeneficiaryAccountNo(),
                    beneficiaryBalance.add(command.getAmount()));
        } else {
            throw new InsufficientBalanceException(sourceBalance, command.getAmount());
        }
    }

    private BigDecimal readAccountBalance(String accountNo) {
        try {
            return accountRepository.getBalanceForUpdateByAccountNumber(accountNo);
        } catch (IncorrectResultSizeException e) {
            throw new UnknownAccountException(accountNo);
        }
    }
}
