package com.example.dbperformancetest.postgresql.repo

import com.example.dbperformancetest.postgresql.MatchResultPostgres
import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import com.example.dbperformancetest.shared.MatchResultCriteriaRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.persistence.criteria.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ObjectUtils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@Repository
class MatchResultCriteriaPostgresRepository : MatchResultCriteriaRepository<MatchResultPostgres>{

    @Autowired
    @Qualifier("PostgresEntityManager")
    private lateinit var entityManagerFactory: LocalContainerEntityManagerFactoryBean

    private lateinit var criteriaBuilder: CriteriaBuilder

    override fun getAllMatchResultsWithFilters(matchFilter: MatchFilter): List<MatchResultPostgres>{
        val entityManager = entityManagerFactory.`object`!!.createEntityManager()

        criteriaBuilder = entityManager.criteriaBuilder

        val criteriaQuery: CriteriaQuery<MatchResultPostgres> = criteriaBuilder.createQuery(MatchResultPostgres::class.java)
        val matchRoot: Root<MatchResultPostgres> = criteriaQuery.from(MatchResultPostgres::class.java)

        val predicate: Predicate = getPredicate(matchFilter, matchRoot)
        criteriaQuery.where(predicate)

        val typedQuery: TypedQuery<MatchResultPostgres> = entityManager.createQuery(criteriaQuery)
        return typedQuery.resultList
    }

    @Transactional("PostgresTransactionManager")
    override fun removeMatchResultsWithFilters(matchFilter: MatchFilter){
        val entityManager = entityManagerFactory.`object`!!.createEntityManager()

        entityManager.transaction.begin()

        criteriaBuilder = entityManager.criteriaBuilder

        val criteriaDelete: CriteriaDelete<MatchResultPostgres> = criteriaBuilder.createCriteriaDelete(MatchResultPostgres::class.java)
        val matchRoot: Root<MatchResultPostgres> = criteriaDelete.from(MatchResultPostgres::class.java)

        val predicate: Predicate = getPredicate(matchFilter, matchRoot)
        criteriaDelete.where(predicate)

        entityManager.createQuery(criteriaDelete).executeUpdate()
        entityManager.transaction.commit()
    }

    private fun <T> getPredicate(matchFilter: MatchFilter, matchRoot: Root<T>): Predicate{

        val predicates: ArrayList<Predicate> = ArrayList<Predicate>()

        if(!ObjectUtils.isEmpty(matchFilter.nationality)){
            predicates += criteriaBuilder.like(
                matchRoot.get("nationality"), "%${matchFilter.nationality}%"
            )
        }

        if(!ObjectUtils.isEmpty(matchFilter.tournamentName)){
            predicates += criteriaBuilder.like(
                matchRoot.get("tournamentName"), "%${matchFilter.tournamentName}%"
            )
        }

        if(!ObjectUtils.isEmpty(matchFilter.soccerTeam)){
            val firstTeamPredicate = criteriaBuilder.like(
                matchRoot.get("firstSoccerTeamName"), "%${matchFilter.soccerTeam}%"
            )

            val secondTeamPredicate = criteriaBuilder.like(
                matchRoot.get("secondSoccerTeamName"), "%${matchFilter.soccerTeam}%"
            )

            predicates += criteriaBuilder.or(firstTeamPredicate, secondTeamPredicate)
        }

        if(!ObjectUtils.isEmpty(matchFilter.startDate) && !ObjectUtils.isEmpty(matchFilter.endDate)){

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val startDateTime: LocalDateTime = LocalDateTime.parse(matchFilter.startDate + "T00:00:00.000Z", formatter)
            val endDateTime: LocalDateTime = LocalDateTime.parse(matchFilter.endDate + "T00:00:00.000Z", formatter)

            val startDate: Date = Date.from(startDateTime.toInstant(ZoneOffset.UTC))
            val endDate: Date = Date.from(endDateTime.toInstant(ZoneOffset.UTC))

            predicates += criteriaBuilder.between(
                matchRoot.get<Date>("date"),startDate, endDate
            )
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}