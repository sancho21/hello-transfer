package id.web.michsan.hellotransfer.repo;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcHelperTest
{
    private JdbcHelper helper;

    @Before
    public void before()
    {
        helper = new JdbcHelper(dataSource());
        helper.transactionBegin();
        try
        {
            helper.executeUpdate("CREATE TABLE students (id INT NOT NULL, name VARCHAR(50))");
        }
        catch (Exception e)
        {
            // The table is already created
        }
    }

    @After
    public void after()
    {
        helper.transactionEnd(false);
    }

    @Test
    public void executeQueryForObject_shouldBeSuccessful()
    {
        helper.executeUpdate("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        String foundName = helper.executeQueryForObject("SELECT name FROM students WHERE id = ?", String.class, 1);

        assertThat(foundName).isEqualTo("Nina");
    }

    @Test
    public void executeQueryForObject_throwExceptionIfNotExactlyOne()
    {
        // Given there are more than one record having the same name
        helper.executeUpdate("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        helper.executeUpdate("INSERT INTO students (id, name) VALUES (2, 'Nina')");

        // When querying for a single object with that name
        // Then an exception is thrown
        assertThatThrownBy(() ->
                helper.executeQueryForObject("SELECT name FROM students WHERE name = ?", String.class, "Nina")
        ).isInstanceOf(IncorrectResultSizeException.class);
    }

    @Test
    public void executeUpdate_shouldBeSuccessful()
    {
        int numOfAffectedRows = helper.executeUpdate("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        assertThat(numOfAffectedRows).isEqualTo(1);
    }

    private DataSource dataSource()
    {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:junit");
        return ds;
    }
}
