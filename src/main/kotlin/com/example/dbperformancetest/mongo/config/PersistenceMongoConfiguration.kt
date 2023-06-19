package com.example.dbperformancetest.mongo.config

import com.example.dbperformancetest.mongo.repo.MatchResultMongoRepository
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(
    basePackageClasses = [MatchResultMongoRepository::class],
    mongoTemplateRef = "MongoMatchResultTemplate"
)
class PersistenceMongoConfiguration {

    @Primary
    @Bean(name = ["MongoProperties"])
    @ConfigurationProperties(prefix = "spring.mongodb.datasource")
    fun matchResultMongoDataSource(): MongoProperties {
        return MongoProperties();
    }

    @Bean(name = ["MongoClient"])
    fun mongoClient(@Qualifier("MongoProperties") mongoProperties: MongoProperties) : MongoClient{
        val credential: MongoCredential = MongoCredential.createCredential(
            mongoProperties.username,
            mongoProperties.authenticationDatabase,
            mongoProperties.password
        )

        return MongoClients.create(MongoClientSettings.builder().applyToClusterSettings { build ->
            build.hosts(
                mutableListOf(
                    ServerAddress(mongoProperties.host, mongoProperties.port)
                )
            )
        }.credential(credential).build()
        )
    }

    @Bean(name = ["MongoFactory"])
    fun mongoDatabaseFactory(@Qualifier("MongoClient") mongoClient: MongoClient,
                             @Qualifier("MongoProperties") mongoProperties: MongoProperties): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.database)
    }

    @Bean(name = ["mongoTransactionManager"])
    fun mongoTransactionManager(@Qualifier("MongoFactory") mongoDatabaseFactory: MongoDatabaseFactory): MongoTransactionManager {
        return MongoTransactionManager(mongoDatabaseFactory)
    }

    @Bean(name = ["MongoMatchResultTemplate"])
    fun mongoTemplate(@Qualifier("MongoFactory") mongoDatabaseFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory)
    }
}