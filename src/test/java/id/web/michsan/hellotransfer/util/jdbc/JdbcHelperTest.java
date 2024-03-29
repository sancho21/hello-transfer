package id.web.michsan.hellotransfer.util.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcHelperTest {
    private JdbcHelper helper;

    @BeforeEach
    public void before() {
        helper = new JdbcHelper(dataSource());
        helper.transactionBegin();
        try {
            helper.update("CREATE TABLE students (id INT NOT NULL, name VARCHAR(50))");
        } catch (Exception e) {
            // The table is already created
        }
    }

    @AfterEach
    public void after() {
        helper.transactionEnd(false);
    }

    @DisplayName("executeQueryForObject - Success case")
    @Test
    public void executeQueryForObject_shouldBeSuccessful() {
        helper.update("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        String foundName = helper.queryForObject("SELECT name FROM students WHERE id = ?", String.class, 1);

        assertThat(foundName).isEqualTo("Nina");
    }

    @DisplayName("executeQueryForObject - Should throw exception if not exactly one")
    @Test
    public void executeQueryForObject_throwExceptionIfNotExactlyOne() {
        // Given there are more than one record having the same name
        helper.update("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        helper.update("INSERT INTO students (id, name) VALUES (2, 'Nina')");

        // When querying for a single object with that name
        // Then an exception is thrown
        assertThatThrownBy(() ->
                helper.queryForObject("SELECT name FROM students WHERE name = ?", String.class, "Nina")
        ).isInstanceOf(IncorrectResultSizeException.class);
    }

    @DisplayName("executeUpdate - Success case")
    @Test
    public void executeUpdate_shouldBeSuccessful() {
        int numOfAffectedRows = helper.update("INSERT INTO students (id, name) VALUES (1, 'Nina')");
        assertThat(numOfAffectedRows).isEqualTo(1);
    }

    private DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:junit");
        return ds;
    }
}
