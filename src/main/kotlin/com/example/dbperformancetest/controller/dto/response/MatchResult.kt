package com.example.dbperformancetest.controller.dto.response

data class MatchResult(
     var nationality: String,
     var tournamentName: String,
     var firstSoccerTeamName: String,
     var secondSoccerTeamName: String,
     var totalScore: String,
     var goals: String,
     var date: String
)