package id.web.michsan.hellotransfer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class AccountModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:app");
        return ds;
    }
}
