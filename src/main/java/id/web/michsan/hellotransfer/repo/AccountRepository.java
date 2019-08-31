package id.web.michsan.hellotransfer.repo;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class AccountRepository {
    private JdbcHelper jdbcHelper;

    public BigDecimal getBalanceForUpdateByAccountNumber(String accountNumber) {
        return jdbcHelper.executeQueryForObject("SELECT balance FROM accounts WHERE number = ? FOR UPDATE",
                BigDecimal.class, accountNumber);
    }

    public BigDecimal getBalanceByAccountNumber(String accountNumber) {
        return jdbcHelper.executeQueryForObject("SELECT balance FROM accounts WHERE number = ?",
                BigDecimal.class, accountNumber);
    }

    public void updateBalance(String accountNo, BigDecimal balance) {
        jdbcHelper.executeUpdate("UPDATE accounts SET balance = ? WHERE number = ?", balance, accountNo);
    }

    public void setDataSource(DataSource dataSource) {
        jdbcHelper = new JdbcHelper(dataSource);
    }
}
