package id.web.michsan.hellotransfer.repo;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcHelperTest
{

    private JdbcHelper helper = new JdbcHelper();

    @Before
    public void before()
    {
        helper.setDatabaseUrl("jdbc:h2:mem:junit");
    }

    @Test
    public void executeQueryForObject_shouldBeSuccessful()
    {
        String foundName = helper.doInConnection(connection -> {
            JdbcHelper.executeUpdate(connection, "CREATE TABLE students (id INT NOT NULL, name VARCHAR(50))");
            JdbcHelper.executeUpdate(connection, "INSERT INTO students (id, name) VALUES (1, 'Nina')");
            return JdbcHelper.executeQueryForObject(connection, "SELECT name FROM students WHERE id = ?", String.class, 1);
        });

        assertThat(foundName).isEqualTo("Nina");
    }

    @Test
    public void executeQueryForObject_throwExceptionIfNotExactlyOne()
    {
        assertThatThrownBy(() ->
                        helper.doInConnection(connection -> {
                            // Given there are more than one record having the same name
                            JdbcHelper.executeUpdate(connection, "CREATE TABLE students (id INT NOT NULL, name VARCHAR(50))");
                            JdbcHelper.executeUpdate(connection, "INSERT INTO students (id, name) VALUES (1, 'Nina')");
                            JdbcHelper.executeUpdate(connection, "INSERT INTO students (id, name) VALUES (2, 'Nina')");

                            // When querying for a single object with that name
                            return JdbcHelper.executeQueryForObject(connection, "SELECT name FROM students WHERE name = ?", String.class, "Nina");
                        })
                // Then an exception is thrown
        ).isInstanceOf(IncorrectResultSizeException.class);
    }

    @Test
    public void executeUpdate_shouldBeSuccessful()
    {
        int numOfAffectedRows = helper.doInConnection(connection -> {
            JdbcHelper.executeUpdate(connection, "CREATE TABLE students (id INT NOT NULL, name VARCHAR(50))");
            return JdbcHelper.executeUpdate(connection, "INSERT INTO students (id, name) VALUES (1, 'Nina')");
        });

        assertThat(numOfAffectedRows).isEqualTo(1);
    }
}
