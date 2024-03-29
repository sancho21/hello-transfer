package id.web.michsan.hellotransfer.util.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionUtils {
    // To hold all variables bound to a thread
    private static final ThreadLocal<Map<Object, Object>> resources =
            ThreadLocal.withInitial(() -> new HashMap<>());

    /**
     * Get connection owned by the current thread or create a new one if it doesn't exist before
     *
     * @param dataSource DataSource to get the connection from
     * @return The connection
     */
    public static Connection getConnection(DataSource dataSource) {
        Map<Object, Object> threadResources = resources.get();
        Connection connection = (Connection) threadResources.get(dataSource);

        if (connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to create connection", e);
            }
            threadResources.put(dataSource, connection);
        }

        return connection;
    }

    public static void releaseConnection(Connection connection, DataSource dataSource) {
        try {
            Map<Object, Object> threadResources = resources.get();
            Connection dsConn = (Connection) threadResources.get(dataSource);
            if (dsConn != null) {
                if (dsConn.getAutoCommit()) {
                    connection.close();
                    threadResources.remove(dataSource);
                } else {
                    // Let it be closed by transaction
                    return;
                }
            } else {
                connection.close();
            }
        } catch (SQLException e) {
            // Just ignore
        }
    }

}
