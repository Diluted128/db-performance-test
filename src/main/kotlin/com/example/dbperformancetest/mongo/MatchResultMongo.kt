package com.example.dbperformancetest.mongo

import org.springframework.data.annotation.Id

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.format.annotation.DateTimeFormat
import java.util.*


@Document("matchResult")
data class MatchResultMongo (

    @Id
    var id: String? = null,

    @Field(name ="nationality")
    var nationality: String = "",

    @Field(name = "tournament_name")
    var tournamentName: String = "",

    @Field(name = "first_soccer_team_name")
    var firstSoccerTeamName: String = "",

    @Field(name = "second_soccer_team_name")
    var secondSoccerTeamName: String = "",

    @Field(name = "total_score")
    var totalScore: String = "",

    @Field(name = "goals")
    var goals: String = "",

    @Field(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var date: Date = Date()
)