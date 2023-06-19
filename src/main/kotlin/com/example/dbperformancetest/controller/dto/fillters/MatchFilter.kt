package com.example.dbperformancetest.controller.dto.fillters


data class MatchFilter(
    var nationality: String = "",
    var tournamentName: String = "",
    var soccerTeam: String = "",
    var startDate: String = "",
    var endDate: String = ""
)