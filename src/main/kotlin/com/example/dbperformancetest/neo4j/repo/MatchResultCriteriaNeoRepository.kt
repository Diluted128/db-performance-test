package com.example.dbperformancetest.neo4j.repo;

import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import com.example.dbperformancetest.neo4j.MatchResultNeo
import com.example.dbperformancetest.shared.MatchResultCriteriaRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ObjectUtils

@Repository
class MatchResultCriteriaNeoRepository : MatchResultCriteriaRepository<MatchResultNeo>{

    @Autowired
    @Qualifier("neo4jClient")
    private lateinit var neo4jClient: Neo4jClient

    @Autowired
    @Qualifier("neo4jMatchResultTemplate")
    private lateinit var neo4jMatchResultTemplate: Neo4jTemplate

    override fun getAllMatchResultsWithFilters(matchFilter: MatchFilter): List<MatchResultNeo>{
        val criteria = getCriteriaQuery(matchFilter) + " RETURN n"
        return neo4jMatchResultTemplate.findAll(criteria, MatchResultNeo::class.java)
    }

    @Transactional("neo4JTransactionManager")
    override fun removeMatchResultsWithFilters(matchFilter: MatchFilter){
        val criteria = getCriteriaQuery(matchFilter) + " DETACH DELETE n"
        neo4jClient.query(criteria).fetch().all()
    }

    private fun getCriteriaQuery(matchFilter: MatchFilter): String {
        var query = "MATCH (n)"
        val listOfConditions: MutableList<String> = mutableListOf()

        if(!ObjectUtils.isEmpty(matchFilter.nationality)){
            listOfConditions += "n.nationality = '${matchFilter.nationality}'"
        }

        if(!ObjectUtils.isEmpty(matchFilter.tournamentName)){
            listOfConditions += "n.tournamentName = '${matchFilter.tournamentName}'"
        }

        if(!ObjectUtils.isEmpty(matchFilter.soccerTeam)){
            listOfConditions += "n.firstSoccerTeamName = '${matchFilter.soccerTeam}' OR " +
                    "n.secondSoccerTeamName = '${matchFilter.soccerTeam}'"
        }

        if(!ObjectUtils.isEmpty(matchFilter.startDate) && !ObjectUtils.isEmpty(matchFilter.endDate)){
            listOfConditions += "n.date >= '${matchFilter.startDate}' AND " +
                    "n.date <= '${matchFilter.endDate}'"
        }

        if(listOfConditions.isNotEmpty()){
            query += " WHERE "
        }

        query += listOfConditions.joinToString(" AND ")

        println(query)
        return query
    }
}
