// src/main/java/com/example/demo/config/R2dbcDataInitializer.java
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class R2dbcDataInitializer implements CommandLineRunner {

    private final R2dbcEntityTemplate entityTemplate;

    // Пути к файлам schema.sql и data.sql из application.properties
    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Value("classpath:data.sql")
    private Resource dataSql;

    public R2dbcDataInitializer(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Читаем содержимое schema.sql
        String schemaScript = StreamUtils.copyToString(schemaSql.getInputStream(), StandardCharsets.UTF_8);
        // Читаем содержимое data.sql
        String dataScript = StreamUtils.copyToString(dataSql.getInputStream(), StandardCharsets.UTF_8);

        // Выполняем скрипты
        // entityTemplate.getDatabaseClient().sql(schemaScript).then().block(); // <-- НЕПРАВИЛЬНО
        // entityTemplate.getDatabaseClient().sql(dataScript).then().block();   // <-- НЕПРАВИЛЬНО

        // ПРАВИЛЬНО: Разбиваем скрипт на отдельные команды и выполняем их по одной
        String[] schemaStatements = schemaScript.split(";");
        String[] dataStatements = dataScript.split(";");

        // Выполняем schema.sql
        for (String statement : schemaStatements) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                entityTemplate.getDatabaseClient().sql(statement).fetch().rowsUpdated().block(); // <-- Блокирующий вызов для инициализации
            }
        }

        // Выполняем data.sql
        for (String statement : dataStatements) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                entityTemplate.getDatabaseClient().sql(statement).fetch().rowsUpdated().block(); // <-- Блокирующий вызов для инициализации
            }
        }

        System.out.println("R2DBC Data Initializer: schema.sql and data.sql executed successfully.");
    }
}