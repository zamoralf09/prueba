package com.umg.seguridadbravo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.umg.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    //la configuraion de la base de datos se encuentra en application.properties
    //tomar en cuenta plis
}
