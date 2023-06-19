# db-performance-test
Performance test of three dbs using own JPA interface implementation. Three databases were tested: PostgreSQL, MongoDb and NeoJS.

### Backend structure

```
├── mongo 
│   ├── config 
│   ├── models 
│   └── repositories
│ 
├── neo4j
│   ├── config
│   ├── models
│   └── repositories
│ 
├── postgresql
│   ├── config
│   ├── models
│   └── repositories
│ 
├── service
│ 
├── controller
│   └── dto 
|       ├── response
│       └── fillter 
│ 
└── shared

```
