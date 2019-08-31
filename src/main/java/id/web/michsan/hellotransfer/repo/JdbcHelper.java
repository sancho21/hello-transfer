package id.web.michsan.hellotransfer.repo;

import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcHelper {
    @Getter
    private DataSource dataSource;

    public JdbcHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int executeUpdate(String query, Object... args) {
        return doInConnection((connection) -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    statement.setObject(i + 1, arg);
                }
                return statement.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to execute query " + query, e);
            }
        });
    }

    public <T> List<T> executeQuery(String query, Class<T> returnType, Object... args) {
        return doInConnection((connection) -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    statement.setObject(i + 1, arg);
                }

                List<T> resultList = new ArrayList<>();
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        resultList.add(rs.getObject(1, returnType));
                    }
                }
                return resultList;
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to execute query " + query, e);
            }
        });
    }

    public <T> T executeQueryForObject(String query, Class<T> returnType, Object... args) {
        List<T> resultList = executeQuery(query, returnType, args);
        if (resultList.size() != 1) {
            throw new IncorrectResultSizeException(1, resultList.size());
        }

        return resultList.get(0);
    }

    public <T> T doInConnection(Function<Connection, T> executions) {
        Connection connection = ConnectionUtils.getConnection(dataSource);
        try {
            return executions.apply(connection);
        } finally {
            ConnectionUtils.releaseConnection(connection);
        }
    }

    public void transactionBegin() {
        Connection connection = ConnectionUtils.getConnection(dataSource);
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to start transaction", e);
        }
    }

    public void transactionEnd(boolean commit) {
        Connection connection = ConnectionUtils.getConnection(dataSource);
        try {
            if (commit) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to commit transaction", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to restore the world order (autocommit)", e);
            }
        }
    }
}
