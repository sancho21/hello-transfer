package id.web.michsan.hellotransfer.repo;

import id.web.michsan.hellotransfer.model.Account;
import id.web.michsan.hellotransfer.util.jdbc.JdbcHelper;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.math.BigDecimal;

public class AccountRepository {
    private JdbcHelper jdbcHelper;

    public BigDecimal getBalanceForUpdateByAccountNumber(String accountNumber) {
        return jdbcHelper.queryForObject("SELECT balance FROM accounts WHERE number = ? FOR UPDATE",
                BigDecimal.class, accountNumber);
    }

    public Account findByAccountNumber(String accountNumber) {
        return jdbcHelper.queryForObject("SELECT * FROM accounts WHERE number = ?",
                rs -> new Account(rs.getString("number"), rs.getString("holder"), rs.getBigDecimal("balance")),
                accountNumber);
    }

    public void updateBalance(String accountNo, BigDecimal balance) {
        jdbcHelper.update("UPDATE accounts SET balance = ? WHERE number = ?", balance, accountNo);
    }

    public void create(Account account) {
        jdbcHelper.update("INSERT INTO accounts (number, holder, balance) VALUES (?, ?, ?)",
                account.getNumber(), account.getHolderName(), account.getBalance());
    }

    @Inject
    public void setDataSource(DataSource dataSource) {
        jdbcHelper = new JdbcHelper(dataSource);

        prepareTablesIfNotExist();
    }

    private void prepareTablesIfNotExist() {
        try {
            jdbcHelper.update("CREATE TABLE accounts (number VARCHAR(50) NOT NULL, holder VARCHAR(100), balance " +
                    "DECIMAL(20, 2))");
        } catch (IllegalStateException e) {
            // Already created
        }
    }

}
