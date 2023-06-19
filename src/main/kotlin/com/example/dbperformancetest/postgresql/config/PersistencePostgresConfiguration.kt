package com.example.dbperformancetest.postgresql.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(
        basePackages = ["com.example.dbperformancetest.postgresql.repo"],
        entityManagerFactoryRef = "PostgresEntityManager",
        transactionManagerRef = "PostgresTransactionManager"
)
class PersistencePostgresConfiguration {

    @Value("\${spring.postgres.datasource.driver-class-name}")
    private lateinit var driver: String

    @Value("\${spring.postgres.datasource.jdbcUrl}")
    private lateinit var uri: String

    @Value("\${spring.postgres.datasource.username}")
    private lateinit var username: String

    @Value("\${spring.postgres.datasource.password}")
    private lateinit var password: String

    @Bean(name = ["PostgresDataSource"])
    fun matchResultPostgresDataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(driver)
        dataSource.url = uri
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }

    @Bean(name = ["PostgresEntityManager"])
    fun entityManagerFactory(builder: EntityManagerFactoryBuilder, @Qualifier("PostgresDataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val properties = hashMapOf<String, String>()
        properties["hibernate.hbm2ddl.auto"] = "update"
        properties["hibernate.dialect"] = "org.hibernate.dialect.PostgreSQLDialect"

        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.example.dbperformancetest")
                .persistenceUnit("SoccerMatchResult")
                .build()
    }

    @Bean(name = ["PostgresTransactionManager"])
    fun matchResultPostgresTransactionManager(@Qualifier("PostgresEntityManager") entityManager: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManager)
    }
}
