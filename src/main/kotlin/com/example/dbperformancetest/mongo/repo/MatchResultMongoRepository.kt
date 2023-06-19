package com.example.dbperformancetest.mongo.repo

import com.example.dbperformancetest.mongo.MatchResultMongo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchResultMongoRepository : MongoRepository<MatchResultMongo, Long>



