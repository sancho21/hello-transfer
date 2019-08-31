package id.web.michsan.hellotransfer.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T toObject(ResultSet rs) throws SQLException;
}
