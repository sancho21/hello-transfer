package id.web.michsan.hellotransfer.repo;

import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcHelper
{
    @Setter
    private String databaseUrl;
    @Setter
    private String databaseUsername;
    @Setter
    private String databasePassword;

    public static int executeUpdate(Connection connection, String query, Object... args)
    {
        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                statement.setObject(i + 1, arg);
            }
            return statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Failed to execute query " + query, e);
        }
    }

    public static <T> List<T> executeQuery(Connection connection, String query, Class<T> returnType, Object... args)
    {
        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                statement.setObject(i + 1, arg);
            }

            List<T> resultList = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery())
            {
                while (rs.next())
                {
                    resultList.add(rs.getObject(1, returnType));
                }
            }
            return resultList;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Failed to execute query " + query, e);
        }
    }

    public static <T> T executeQueryForObject(Connection connection, String query, Class<T> returnType, Object... args)
    {
        List<T> resultList = executeQuery(connection, query, returnType, args);
        if (resultList.size() != 1)
        {
            throw new IncorrectResultSizeException(1, resultList.size());
        }

        return resultList.get(0);
    }

    public <T> T doInConnection(Function<Connection, T> executions)
    {
        try (Connection connection = DriverManager
                .getConnection(databaseUrl, databaseUsername, databasePassword))
        {
            return executions.apply(connection);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Failed to execute function", e);
        }
    }
}
