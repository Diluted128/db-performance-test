package com.example.dbperformancetest.shared

import com.example.dbperformancetest.controller.dto.fillters.MatchFilter
import com.example.dbperformancetest.mongo.MatchResultMongo

interface MatchResultCriteriaRepository<T> {

    fun getAllMatchResultsWithFilters(matchFilter: MatchFilter): List<T>

    fun removeMatchResultsWithFilters(matchFilter: MatchFilter)
}