package com.example.dbperformancetest.mongo.repo

import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import com.example.dbperformancetest.mongo.MatchResultMongo
import com.example.dbperformancetest.shared.MatchResultCriteriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


@Repository
class MatchResultCriteriaMongoRepository : MatchResultCriteriaRepository<MatchResultMongo>{

    @Autowired
    @Qualifier("MongoMatchResultTemplate")
    private lateinit var mongoTemplate: MongoTemplate

    override fun getAllMatchResultsWithFilters(matchFilter: MatchFilter): List<MatchResultMongo>{
        val query = getCriteriaQuery(matchFilter)
        return mongoTemplate.find(query, MatchResultMongo::class.java)
    }


    override fun removeMatchResultsWithFilters(matchFilter: MatchFilter){
        val query = getCriteriaQuery(matchFilter)
        mongoTemplate.remove(query, MatchResultMongo::class.java)
    }

    private fun getCriteriaQuery(matchFilter: MatchFilter): Query{
        var query = Query()

        if(!ObjectUtils.isEmpty(matchFilter.nationality)){
            query.addCriteria(Criteria.where("nationality").`is`(matchFilter.nationality))
        }

        if(!ObjectUtils.isEmpty(matchFilter.tournamentName)){
            query.addCriteria(Criteria.where("tournamentName").`is`(matchFilter.tournamentName))
        }

        if(!ObjectUtils.isEmpty(matchFilter.soccerTeam)){
            val firstTeamCriteria = Criteria.where("firstSoccerTeamName").`is`(matchFilter.soccerTeam)
            val secondTeamCriteria = Criteria.where("secondSoccerTeamName").`is`(matchFilter.soccerTeam)

            query.addCriteria(Criteria().orOperator(firstTeamCriteria, secondTeamCriteria))
        }

        if(!ObjectUtils.isEmpty(matchFilter.startDate) && !ObjectUtils.isEmpty(matchFilter.endDate)){
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            val startDateTime: LocalDateTime = LocalDateTime.parse(matchFilter.startDate + "T00:00:00.000Z", formatter)
            val endDateTime: LocalDateTime = LocalDateTime.parse(matchFilter.endDate + "T00:00:00.000Z", formatter)

            val startDate: Date = Date.from(startDateTime.toInstant(ZoneOffset.UTC))
            val endDate: Date = Date.from(endDateTime.toInstant(ZoneOffset.UTC))

            query.addCriteria(Criteria().andOperator(
                Criteria.where("date").gte(startDate),
                Criteria.where("date").lte(endDate)
            ))
        }

        return query
    }
}