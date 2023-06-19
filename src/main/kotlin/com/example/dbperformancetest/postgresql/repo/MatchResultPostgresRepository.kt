package com.example.dbperformancetest.postgresql.repo

import com.example.dbperformancetest.postgresql.MatchResultPostgres
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MatchResultPostgresRepository : JpaRepository<MatchResultPostgres, Long> {
}