package com.vicchen.spring.boot.primary.replica.config;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
@RequiredArgsConstructor
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final List<DataSource> replicas;
    private int counter = 0;

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }

    @NonNull
    @Override
    protected DataSource determineTargetDataSource() {
        if (DataSourceContextHolder.get() == DataSourceType.REPLICA && !replicas.isEmpty()) {
            int idx = counter++ % replicas.size();
            DataSource replica = replicas.get(idx);
            logDataSourceInfo(replica, "REPLICA-" + (idx + 1));
            return replica;
        }

        DataSource defaultDataSource = getResolvedDefaultDataSource();
        if (defaultDataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [PRIMARY]");
        }
        logDataSourceInfo(defaultDataSource, "PRIMARY");
        return defaultDataSource;
    }

    private void logDataSourceInfo(DataSource dataSource, String dbType) {
        try {
            if (dataSource instanceof HikariDataSource hikari) {
                String url = hikari.getJdbcUrl();
                String poolName = hikari.getPoolName();
                log.info("🔍 [DB-ROUTING] Using {} - URL: {} (Pool: {})", dbType, url, poolName);
            } else {
                log.info("🔍 [DB-ROUTING] Using {} - DataSource: {}", dbType, dataSource.getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.info("🔍 [DB-ROUTING] Using {} - (Unable to get connection info)", dbType);
        }
    }
}
