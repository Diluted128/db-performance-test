package com.example.dbperformancetest.neo4j.config

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.DatabaseSelectionProvider
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories


@Configuration
@EnableNeo4jRepositories(
    basePackages = ["com.example.dbperformancetest.neo4j.repo"],
    transactionManagerRef = "neo4JTransactionManager",
    neo4jTemplateRef = "neo4jMatchResultTemplate"
)
class PersistenceNeoConfiguration : Neo4jDataAutoConfiguration() {

    @Value("\${spring.neo4j.datasource.url}")
    private lateinit var uri: String

    @Value("\${spring.neo4j.datasource.authentication.username}")
    private lateinit var username: String

    @Value("\${spring.neo4j.datasource.authentication.password}")
    private lateinit var password: String

    @Bean()
    fun getConfiguration(): Driver {
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password))
    }

    @Bean(name = ["neo4JTransactionManager"])
    fun neo4jTransactionManager(): Neo4jTransactionManager {
        return Neo4jTransactionManager(getConfiguration())
    }

    @Bean(name = ["neo4jClient"])
    fun neo4jClient(databaseNameProvider: DatabaseSelectionProvider): Neo4jClient{
        return Neo4jClient.create(getConfiguration(), databaseNameProvider)
    }

    @Bean("neo4jMatchResultTemplate")
    fun neo4jMatchResultTemplate(@Qualifier("neo4jClient") neo4jClient: Neo4jClient,
                            neo4jMappingContext: Neo4jMappingContext): Neo4jTemplate{
        return Neo4jTemplate(neo4jClient, neo4jMappingContext)
    }
}