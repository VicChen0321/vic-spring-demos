package com.vicchen.spring.boot.primary.replica.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.replica1")
    public DataSourceProperties replica1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource replica1DataSource() {
        return replica1DataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.replica2")
    public DataSourceProperties replica2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource replica2DataSource() {
        return replica2DataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }


    @Bean
    public DataSource routingDataSource(
        @Qualifier("primaryDataSource") DataSource primaryDataSource,
        @Qualifier("replica1DataSource") DataSource replica1DataSource,
        @Qualifier("replica2DataSource") DataSource replica2DataSource
    ) {
        RoutingDataSource routing = new RoutingDataSource(List.of(replica1DataSource, replica2DataSource));
        routing.setTargetDataSources(Map.of(
            DataSourceType.PRIMARY, primaryDataSource,
            DataSourceType.REPLICA, replica1DataSource  // 先用 replica1 當預設
        ));
        routing.setDefaultTargetDataSource(primaryDataSource);
        return routing;
    }
}
