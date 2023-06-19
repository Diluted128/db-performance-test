package com.example.dbperformancetest.controller.dto.response;

import com.example.dbperformancetest.mongo.MatchResultMongo
import com.example.dbperformancetest.neo4j.MatchResultNeo
import com.example.dbperformancetest.postgresql.MatchResultPostgres

data class QueryExecutionTimeResult(
        val postgresQueryExecutionTime: Long = 0,
        val mongoQueryExecutionTime: Long = 0,
        val nqo4jQueryExecutionTime: Long = 0,
        val postgresQueryExecutionTimeMessage: String = "",
        val mongoQueryExecutionTimeMessage: String = "",
        val nqo4jQueryExecutionTimeMessage: String = "",
        val postgresData: List<MatchResultPostgres> = emptyList(),
        val mongoData: List<MatchResultMongo> = emptyList(),
        val neoData: List<MatchResultNeo> = emptyList()
)
