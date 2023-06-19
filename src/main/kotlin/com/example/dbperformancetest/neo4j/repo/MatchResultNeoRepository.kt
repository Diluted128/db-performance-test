package com.example.dbperformancetest.neo4j.repo

import com.example.dbperformancetest.neo4j.MatchResultNeo
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchResultNeoRepository : Neo4jRepository<MatchResultNeo, Long>