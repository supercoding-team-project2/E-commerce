package com.github.supercodingproject2mall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.github.supercodingproject2mall.auth.repository",
        "com.github.supercodingproject2mall.cart.repository",
                "com.github.supercodingproject2mall.cartItem.repository",
                "com.github.supercodingproject2mall.item.repository",
                "com.github.supercodingproject2mall.itemOption.repository",
                "com.github.supercodingproject2mall.optionValue.repository",
                "com.github.supercodingproject2mall.cartOptionValue.repository",
                "com.github.supercodingproject2mall.cartItemOption.repository",
                "com.github.supercodingproject2mall.category.repository",
                "com.github.supercodingproject2mall.order.repository",
                "com.github.supercodingproject2mall.orderItem.repository",
                "com.github.supercodingproject2mall.orderAddress.repository",
                "com.github.supercodingproject2mall.orderPayment.repository",
                "com.github.supercodingproject2mall.sale.repository",
        },
        entityManagerFactoryRef = "entityManagerFactoryBean1"
)
@EnableConfigurationProperties(DataSourceProperties.class)
@RequiredArgsConstructor
public class JpaConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean1() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        em.setPackagesToScan("com.github.supercodingproject2mall.auth.entity",
                "com.github.supercodingproject2mall.cart.entity",
                "com.github.supercodingproject2mall.cartItem.entity",
                "com.github.supercodingproject2mall.category.entity",
                "com.github.supercodingproject2mall.item.entity",
                "com.github.supercodingproject2mall.itemOption.entity",
                "com.github.supercodingproject2mall.order.entity",
                "com.github.supercodingproject2mall.cartItemOption.entity",
                "com.github.supercodingproject2mall.cartOptionValue.entity",
                "com.github.supercodingproject2mall.optionValue.entity",
                "com.github.supercodingproject2mall.category.entity",
                "com.github.supercodingproject2mall.orderItem.entity",
                "com.github.supercodingproject2mall.orderAddress.entity",
                "com.github.supercodingproject2mall.orderPayment.entity",
                "com.github.supercodingproject2mall.sale.entity"
        );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comment", "true");

        em.setJpaPropertyMap(properties);

        return em;
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }
}
