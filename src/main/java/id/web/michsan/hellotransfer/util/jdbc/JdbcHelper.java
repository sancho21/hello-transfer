package id.web.michsan.hellotransfer.util.jdbc;

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

    public int update(String query, Object... args) {
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

    public <T> List<T> query(String query, Class<T> returnType, Object... args) {
        return query(query, rs -> rs.getObject(1, returnType), args);
    }

    public <T> T queryForObject(String query, Class<T> returnType, Object... args) {
        List<T> resultList = query(query, returnType, args);
        if (resultList.size() != 1) {
            throw new IncorrectResultSizeException(1, resultList.size());
        }

        return resultList.get(0);
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... args) {
        return doInConnection((connection) -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    statement.setObject(i + 1, arg);
                }

                List<T> resultList = new ArrayList<>();
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        resultList.add(rowMapper.toObject(rs));
                    }
                }
                return resultList;
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to execute query " + query, e);
            }
        });
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... args) {
        List<T> resultList = query(query, rowMapper, args);
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
            ConnectionUtils.releaseConnection(connection, dataSource);
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
                connection.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to close connection", e);
            }
        }
    }

}
