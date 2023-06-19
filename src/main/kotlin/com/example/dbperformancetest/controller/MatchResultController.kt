package com.example.dbperformancetest.controller

import com.example.dbperformancetest.service.MatchResultService
import com.example.dbperformancetest.controller.dto.response.MatchResult
import com.example.dbperformancetest.controller.dto.response.QueryExecutionTimeResult
import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class MatchResultController {

    @Autowired
    private lateinit var matchResultService: MatchResultService;

    @GetMapping("/matches")
    fun getSoccerMatchResultByCountry(matchFilter: MatchFilter): QueryExecutionTimeResult{
        return matchResultService.getSoccerMatchResult(matchFilter)
    }

    @PostMapping("/matches")
    fun saveMatches(@RequestBody matches: List<MatchResult>): QueryExecutionTimeResult {
        return matchResultService.saveSoccerMatchResults(matches)
    }

    @DeleteMapping("/matches")
    fun removeMatches(matchFilter: MatchFilter): QueryExecutionTimeResult {
        return matchResultService.removeSoccerMatchResults(matchFilter)
    }
}