package id.web.michsan.hellotransfer.command;

import com.zaxxer.hikari.HikariDataSource;
import id.web.michsan.hellotransfer.repo.AccountRepository;
import id.web.michsan.hellotransfer.repo.JdbcHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountCommandHandlerIntegrationTest
{
    private AccountCommandHandler commandHandler;
    private JdbcHelper jdbcHelper;

    @Before
    public void before() throws Exception
    {
        jdbcHelper = new JdbcHelper(dataSource());

        commandHandler = new AccountCommandHandler();
        commandHandler.setAccountRepository(accountRepository());

        prepareTables();
        jdbcHelper.transactionBegin();
    }

    @After
    public void after()
    {
        jdbcHelper.transactionEnd(false);
    }

    private void prepareTables()
    {
        try
        {
            jdbcHelper.executeUpdate("CREATE TABLE accounts (number VARCHAR(50) NOT NULL, balance DECIMAL(20, 2))");
        }
        catch (Exception e)
        {
            // Table already created
        }
    }

    @Test
    public void handleMoneyTransfer_onSufficientAmountShouldBeSuccessful()
    {
        // Given that there are 2 accounts with balance
        createAccountWithBalance("acc01", new BigDecimal("20"));
        createAccountWithBalance("acc02", new BigDecimal("40"));

        // When transferring money not exceeding the source balance
        // Then it should be successful
        MoneyTransferCommand command = new MoneyTransferCommand("acc01", "acc02", new BigDecimal(("20")));
        commandHandler.handle(command);

        // And the new balance should reflects it correctly
        assertThat(jdbcHelper.executeQueryForObject("SELECT balance FROM accounts WHERE number = ?",
                BigDecimal.class, "acc01")).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(jdbcHelper.executeQueryForObject("SELECT balance FROM accounts WHERE number = ?",
                BigDecimal.class, "acc02")).isEqualByComparingTo(new BigDecimal("60"));
    }

    @Test
    public void handleMoneyTransfer_onInsufficientAmountShouldThrowException()
    {
        // Given that there are 2 accounts with balance
        createAccountWithBalance("acc01", new BigDecimal("20"));
        createAccountWithBalance("acc02", new BigDecimal("40"));

        // When transferring money exceeding the source balance
        // Then an exception is thrown
        MoneyTransferCommand command = new MoneyTransferCommand("acc01", "acc02", new BigDecimal(("200")));
        assertThatThrownBy(() -> commandHandler.handle(command)).isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    public void handleMoneyTransfer_onUnknownBeneficiaryAccountShouldThrowException()
    {
        // Given that there is an account with balance
        createAccountWithBalance("acc01", new BigDecimal("20"));

        // When transferring money to and unknown account
        // Then an exception is thrown
        MoneyTransferCommand command = new MoneyTransferCommand("acc01", "acc02", new BigDecimal(("200")));
        assertThatThrownBy(() -> commandHandler.handle(command)).isInstanceOf(UnknownAccountException.class);
    }

    private void createAccountWithBalance(String accountNo, BigDecimal amount)
    {
        jdbcHelper.executeUpdate("INSERT INTO accounts (number, balance) VALUES (?, ?)", accountNo, amount);
    }

    private AccountRepository accountRepository()
    {
        AccountRepository repository = new AccountRepository();
        repository.setDataSource(jdbcHelper.getDataSource());
        return repository;
    }

    private DataSource dataSource()
    {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:junit");
        return ds;
    }

}
