package PizzaHut_be.config.db;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Nonnull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DatabaseConfigProperties dbConfig;

    @Autowired
    private HikariConfigProperties hikariConfigProperties;

    @Bean
    @SneakyThrows
    public DataSource dataSource() {
        String dbUsername = dbConfig.getUsername();
        String dbPassword = dbConfig.getPassword();
        return getHikariDataSource(dbUsername, dbPassword);
    }

    @Nonnull
    private HikariDataSource getHikariDataSource(String dbUsername, String dbPassword) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setJdbcUrl(dbConfig.getJdbcUrl());
        dataSource.setDriverClassName(hikariConfigProperties.getDriverClass());

        // Additional HikariCP configuration
        dataSource.setMinimumIdle(hikariConfigProperties.getMinimumIdle());
        dataSource.setMaximumPoolSize(hikariConfigProperties.getMaximumPoolSize());
        dataSource.setIdleTimeout(hikariConfigProperties.getIdleTimeout());
        dataSource.setMaxLifetime(hikariConfigProperties.getMaxLifetime());
        dataSource.setPoolName(hikariConfigProperties.getPoolName());
        dataSource.setConnectionTestQuery(hikariConfigProperties.getConnectionTestQuery());
        return dataSource;
    }
}
