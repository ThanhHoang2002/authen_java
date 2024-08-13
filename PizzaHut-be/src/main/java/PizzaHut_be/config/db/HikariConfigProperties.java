package PizzaHut_be.config.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hikari")
public class HikariConfigProperties {
    private Integer minimumIdle;
    private Integer maximumPoolSize;
    private Integer idleTimeout;
    private Integer maxLifetime;
    private String connectionTestQuery;
    private String poolName;
    private String driverClass;
}
