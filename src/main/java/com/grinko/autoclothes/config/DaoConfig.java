package com.grinko.autoclothes.config;

import com.grinko.autoclothes.handlers.TxAction;
import com.grinko.autoclothes.util.TxUtil;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static com.grinko.autoclothes.util.DbUtil.requiresNewTx;
import static com.grinko.autoclothes.util.LangUtil.nvl;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloths-datasource", ignoreUnknownFields = false)
public class DaoConfig {

    private String url;
    private String username;
    private String password;
    private int maxPoolSize;
    private int idleTimeout;
    private int minIdle;
    private int connectionTimeout;
    private int leakDetectionThreshold;
    private int maxLifetime;
    private boolean registerMbeans;
    private String poolName;
    private String schema;
    private String connectionInitSql;
    private String connectionTestQuery;
    private boolean baselineOnMigrate;
    private String baselineVersion;

    @Bean
    @Lazy
    DataSource dataSource() {

        var cfg = new HikariConfig();
        cfg.setAutoCommit(false);
        cfg.setJdbcUrl(url);
        cfg.setPassword(password);
        cfg.setUsername(username);
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(maxPoolSize);

        cfg.setIdleTimeout(idleTimeout);
        cfg.setMinimumIdle(minIdle);
        cfg.setConnectionTimeout(connectionTimeout);
        cfg.setLeakDetectionThreshold(leakDetectionThreshold);
        cfg.setMaxLifetime(maxLifetime);
        cfg.setRegisterMbeans(registerMbeans);

        cfg.setConnectionInitSql(connectionInitSql);
        cfg.setConnectionTestQuery(connectionTestQuery);
        cfg.setSchema(schema);
        cfg.setPoolName(poolName);

        return new com.zaxxer.hikari.HikariDataSource(cfg);
    }

    @Bean
    @Lazy
    DataSource txAwareDataSource(final DataSource dataSource) {

        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    @Lazy
    PlatformTransactionManager txManager(final DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Lazy
    @DependsOn("flyway")
    JdbcOperations jdbcOperations(final DataSource dataSource) {

        var res = new JdbcTemplate(dataSource);
        res.setFetchSize(1024);
        return res;
    }

    @Bean(initMethod = "migrate")
    Flyway flyway(final DataSource dataSource) {

        FluentConfiguration cfg = Flyway.configure()
            .dataSource(dataSource)
            .schemas(schema)
            .initSql("CREATE SCHEMA IF NOT EXISTS " + schema + ";")
            .locations("classpath:db/migration");

        if (baselineOnMigrate) {
            cfg = cfg.baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(baselineVersion);
        }

        var fw = cfg.load();

        if (baselineOnMigrate
            && nvl(fw.info(),
            i -> nvl(i.applied(),
                j -> j.length, 0), 0
        ) < 1
        ) {
            fw.baseline();
        }

        return fw;
    }

    @Bean
    @Lazy
    NamedParameterJdbcTemplate namedOperations(final JdbcOperations operations) {

        return new NamedParameterJdbcTemplate(operations);
    }

    @Bean
    @Lazy
    TxUtil txUtil(final PlatformTransactionManager txManager) {

        return nested -> new TxAction(
            requiresNewTx(txManager),
            nested
        );
    }
}
