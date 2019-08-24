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

### Run benchmark

1. Load all uuid into cache
2. Run test
