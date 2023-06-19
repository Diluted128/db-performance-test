package com.example.dbperformancetest.neo4j


import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.support.DateLong
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

@Node("MatchResult")
data class MatchResultNeo (
    @Id
    @GeneratedValue
    var id: Long?,

    var nationality: String = "",

    var tournamentName: String = "",

    var firstSoccerTeamName: String = "",

    var secondSoccerTeamName: String = "",

    var totalScore: String = "",

    var goals: String = "",

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var date: Date = Date()
)