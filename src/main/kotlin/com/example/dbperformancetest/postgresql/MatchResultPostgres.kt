package com.example.dbperformancetest.postgresql

import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

@Entity(name = "MatchResult")
data class MatchResultPostgres(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name ="nationality")
    var nationality: String = "",

    @Column(name = "tournament_name")
    var tournamentName: String = "",

    @Column(name = "first_soccer_team_name")
    var firstSoccerTeamName: String = "",

    @Column(name = "second_soccer_team_name")
    var secondSoccerTeamName: String = "",

    @Column(name = "total_score")
    var totalScore: String = "",

    @Column(name = "goals")
    var goals: String = "",

    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var date: Date = Date()

)



