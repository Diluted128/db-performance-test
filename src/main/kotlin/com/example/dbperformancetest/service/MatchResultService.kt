package com.example.dbperformancetest.service;

import com.example.dbperformancetest.mongo.repo.MatchResultMongoRepository
import com.example.dbperformancetest.mongo.MatchResultMongo
import com.example.dbperformancetest.postgresql.MatchResultPostgres
import com.example.dbperformancetest.postgresql.repo.MatchResultPostgresRepository
import com.example.dbperformancetest.controller.dto.response.MatchResult
import com.example.dbperformancetest.controller.dto.response.QueryExecutionTimeResult
import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import com.example.dbperformancetest.neo4j.MatchResultNeo
import com.example.dbperformancetest.neo4j.repo.MatchResultNeoRepository
import com.example.dbperformancetest.shared.MatchResultCriteriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@Service
class MatchResultService {

    @Autowired
    private lateinit var matchResultPostgresRepository: MatchResultPostgresRepository

    @Autowired
    private lateinit var matchResultMongoRepository: MatchResultMongoRepository

    @Autowired
    private lateinit var matchResultNeoRepository: MatchResultNeoRepository

    @Autowired
    private lateinit var matchResultCriteriaPostgresRepository: MatchResultCriteriaRepository<MatchResultPostgres>

    @Autowired
    private lateinit var matchResultCriteriaMongoRepository: MatchResultCriteriaRepository<MatchResultMongo>

    @Autowired
    private lateinit var matchResultCriteriaNeoRepository: MatchResultCriteriaRepository<MatchResultNeo>

    fun saveSoccerMatchResults(matchResults: List<MatchResult>): QueryExecutionTimeResult  {

        val formatter = SimpleDateFormat("yyyy-MM-dd")

        val postgresEntities = matchResults.stream().map { e -> MatchResultPostgres(
            null, e.nationality,
            e.tournamentName,
            e.firstSoccerTeamName,
            e.secondSoccerTeamName,
            e.totalScore,
            e.goals,
            formatter.parse(e.date)) }.collect(Collectors.toList())

        val mongoEntities = matchResults.stream().map { e -> MatchResultMongo(
            null,
            e.nationality,
            e.tournamentName,
            e.firstSoccerTeamName,
            e.secondSoccerTeamName,
            e.totalScore,
            e.goals,
            formatter.parse(e.date))
        }.collect(Collectors.toList())

        val neoEntities = matchResults.stream().map { e -> MatchResultNeo(
            null,
            e.nationality,
            e.tournamentName,
            e.firstSoccerTeamName,
            e.secondSoccerTeamName,
            e.totalScore,
            e.goals,
            formatter.parse(e.date))
        }.collect(Collectors.toList())

        var postgresQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultPostgresRepository.saveAll(postgresEntities)
        postgresQueryExecutionTime = System.currentTimeMillis() - postgresQueryExecutionTime

        var mongoQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultMongoRepository.saveAll(mongoEntities)
        mongoQueryExecutionTime = System.currentTimeMillis() - mongoQueryExecutionTime

        var neoQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultNeoRepository.saveAll(neoEntities)
        neoQueryExecutionTime = System.currentTimeMillis() - neoQueryExecutionTime

        return getTimeRepresentationObject(
            postgresQueryExecutionTime,
            mongoQueryExecutionTime,
            neoQueryExecutionTime,
            emptyList(),
            emptyList(),
            emptyList()
        )
    }


    fun getSoccerMatchResult(matchFilter: MatchFilter): QueryExecutionTimeResult{
        var postgresQueryExecutionTime: Long = System.currentTimeMillis()
        var resultPostgres: List<MatchResultPostgres> =
            matchResultCriteriaPostgresRepository.getAllMatchResultsWithFilters(matchFilter)
        postgresQueryExecutionTime = System.currentTimeMillis() - postgresQueryExecutionTime

        var mongoQueryExecutionTime: Long = System.currentTimeMillis()
        var resultMongo: List<MatchResultMongo> = matchResultCriteriaMongoRepository.getAllMatchResultsWithFilters(matchFilter)
        mongoQueryExecutionTime = System.currentTimeMillis() - mongoQueryExecutionTime

        var neoQueryExecutionTime: Long = System.currentTimeMillis()
        var neoResult: List<MatchResultNeo> = matchResultCriteriaNeoRepository.getAllMatchResultsWithFilters(matchFilter)
        neoQueryExecutionTime = System.currentTimeMillis() - neoQueryExecutionTime

        return getTimeRepresentationObject(
            postgresQueryExecutionTime,
            mongoQueryExecutionTime,
            neoQueryExecutionTime,
            resultPostgres,
            resultMongo,
            neoResult
        )
    }

    fun removeSoccerMatchResults(matchFilter: MatchFilter): QueryExecutionTimeResult{
        var postgresQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultCriteriaPostgresRepository.removeMatchResultsWithFilters(matchFilter)
        postgresQueryExecutionTime = System.currentTimeMillis() - postgresQueryExecutionTime

        var mongoQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultCriteriaMongoRepository.removeMatchResultsWithFilters(matchFilter)
        mongoQueryExecutionTime = System.currentTimeMillis() - mongoQueryExecutionTime

        var neoQueryExecutionTime: Long = System.currentTimeMillis()
        matchResultCriteriaNeoRepository.removeMatchResultsWithFilters(matchFilter)
        neoQueryExecutionTime = System.currentTimeMillis() - neoQueryExecutionTime

        return getTimeRepresentationObject(
            postgresQueryExecutionTime,
            mongoQueryExecutionTime,
            neoQueryExecutionTime,
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    private fun getTimeRepresentationObject(postgresQueryExecutionTime: Long,
                                            mongoQueryExecutionTime: Long,
                                            neoQueryExecutionTime: Long,
                                            postgresData: List<MatchResultPostgres>,
                                            mongoData: List<MatchResultMongo>,
                                            neoData: List<MatchResultNeo>
    ): QueryExecutionTimeResult {
        return QueryExecutionTimeResult(
            postgresQueryExecutionTime,
            mongoQueryExecutionTime,
            neoQueryExecutionTime,
            getTimeFormat(postgresQueryExecutionTime),
            getTimeFormat(mongoQueryExecutionTime),
            getTimeFormat(neoQueryExecutionTime),
            postgresData,
            mongoData,
            neoData
        )
    }

    private fun getTimeFormat(millis: Long): String{
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)
        val milliSeconds: Long = millis - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds)
        return "$minutes min, $seconds sec, $milliSeconds milliSec"
    }
}
