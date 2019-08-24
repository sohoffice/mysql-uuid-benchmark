Usage
-------

### 1. Create database and table

Execute the following SQL as mysql admin user.

```
CREATE DATABASE uuid CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
CREATE USER 'uuid'@'%' IDENTIFIED BY 'uuid';
GRANT ALL PRIVILEGES ON uuid.* to 'uuid'@'%';

create table uuid.binary_uuid
(
    id   bigint not null,
    uuid binary(16) not null
);

create table uuid.string_uuid
(
    id   bigint not null,
    uuid varchar(36) not null
);

create table uuid.number_uuid
(
    id bigint not null,
    uuid1 bigint not null,
    uuid2 bigint not null
);
```

The above will create 3 variation of tables for our benchmark, but we do
not want the unique index to be added yet.

### Start the spring boot server

```
mvn spring-boot:run
```

### Run this command to start populating data

```
curl -X POST http://localhost:8080/data/prepare
```

### Add unique index to the tables

```
alter table uuid.binary_uuid add constraint binary_uuid_uniq1 unique (uuid);
alter table uuid.string_uuid add constraint string_uuid_uniq1 unique (uuid);
alter table uuid.number_uuid add constraint number_uuid_uniq1 unique (uuid1, uuid2);
```

### Run benchmark

1. Load all uuid into cache

    ```
    curl -X POST http://localhost:8080/data/load
    ```
    
2. Run test

    ```
    # string_uuid
    curl -X POST http://localhost:8080/benchmark/str
    # number_uuid
    curl -X POST http://localhost:8080/benchmark/num
    # binary_uuid
    curl -X POST http://localhost:8080/benchmark/bin
    ```

Sample Results
==============

The below is a preliminary test, running on a desktop PC. This may not 
represent real-world performance, and is only provided for reference.

```
$ curl -X POST http://localhost:8080/data/load

$ curl -X POST http://localhost:8080/benchmark/str
{"count":10000,"sum":5299,"min":0,"max":14,"average":0.5299}

$ curl -X POST http://localhost:8080/benchmark/bin
{"count":10000,"sum":2103,"min":0,"max":4,"average":0.2103}

$ curl -X POST http://localhost:8080/benchmark/str
{"count":10000,"sum":3150,"min":0,"max":6,"average":0.315}

$ curl -X POST http://localhost:8080/benchmark/bin
{"count":10000,"sum":1849,"min":0,"max":1,"average":0.1849}

$ curl -X POST http://localhost:8080/benchmark/str
{"count":10000,"sum":6194,"min":0,"max":39,"average":0.6194}

$ curl -X POST http://localhost:8080/benchmark/bin
{"count":10000,"sum":2109,"min":0,"max":3,"average":0.2109}
```

From this particular result set, BINARY(16) is more performant than the 
VARCHAR(36) version.

---

Follow me on [medium](https://medium.com/sohoffice)
