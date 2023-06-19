package com.example.dbperformancetest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan
class DbPerformanceTestApplication

fun main(args: Array<String>) {
	runApplication<DbPerformanceTestApplication>(*args)
}


