# Fluent JDBC
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.saharsh/fluent-jdbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.saharsh/fluent-jdbc/)
[![Javadoc](http://javadoc-badge.appspot.com/org.saharsh/fluent-jdbc.svg)](http://www.javadoc.io/doc/org.saharsh/fluent-jdbc)

## Why?

This library is intended as a simple Fluent API layer on top of Spring Framework's famous [JDBC Template](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#jdbc-JdbcTemplate). It is ideal when you are looking for a lightweight solution to JDBC access from your Java application where a full blown ORM might seem like overkill. This library is not a replacement for an actual ORM.

The goals of this library are:

- Provide a Fluent interface powered by JdbcTemplate
- Provide ability to reuse the table and column definitions used in JDBC commands throughout your code. Makes your code more maintainable and less error prone.
- Provide an abstraction on top of JdbcTemplate's result mapping interface to avoid repetitive object mapping code.

## How to use?

### FluentJdbc

`org.saharsh.fluentjdbc.FluentJdbc` is the main class you will be interacting with to use this library. It has a factory method, `using(JdbcTemplate jdbcTemplate)`, you can call to create a new instance. Instances of this class are lightweight, immutable, and thread-safe. Feel to create one instance that is reused for the lifetime of the application, or create a new instance every time you start a new command. For brevity, `jdbc` will refer to an instance of this class for the remainder of this README. In essence,

        FluentJdbc jdbc = FluentJdbc.using(jdbcTemplate);

The `jdbc` object supports the following four standard SQL commands:

- `SELECT`
- `INSERT`
- `UPDATE`
- `DELETE`

The `jdbc` object has a factory method for each of these commands. Each factory method returns an implementation of a builder interface for that SQL command. These builder instances are stateful and NOT thread safe. Each has a `build()` method that, if the builder instance has been properly configured, will create an instance of the proper `org.saharsh.fluentjdbc.command.JdbcCommand` subclass. The `JdbcCommand` subclasses are immutable and thread safe.

Examples below will use the following schema:

```sql
CREATE TABLE IF NOT EXISTS PERSON (
    ID CHAR(36),
    NAME VARCHAR(255) NOT NULL,
    AGE INTEGER,
    PRIMARY KEY (ID)
);
```

The library provides a `org.saharsh.fluentjdbc.builder.Column` class that you can use to create and reuse table+column definitions. The schema above can be captured in code via three `Column` instances:

```
Column<String> id = new Column<>("person", "id", String.class);
Column<String> name = new Column<>("person", "name", String.class);
Column<Integer> age = new Column<>("person", "age", Integer.class);
```

### `SELECT`

```
// Find adults
List<ResultRow> results = jdbc.select(id, name, age)
    .withClauses("FROM " + id.getTable() + " WHERE " + age + " >= ?")
    .givenClauseParams(18)
    .build().execute();

// First row of results
String name = results.get(0).get(name);
Integer age = results.get(0).get(age);
```

You'll notice that the `select` method takes a variable number `org.saharsh.fluentjdbc.command.SelectPart` instances. `Column` is a subclass of `SelectPart`. `SelectPart` is more open ended and can be used to express any part of the `SELECT` clause. It also overrides the `java.lang.Object.toString()` method to return the the select expression. This allows the concatenation you see in `withClauses("FROM " + id.getTable() + " WHERE " + age + " >= ?")`, where the `age` instance can be concatenated directly with the rest of the clause. While very useful in cases like this, this becomes even more useful when expressing complicated `JOIN` clauses.

### `INSERT`

```java
// insert ("John Doe", 18)
jdbc.insert(id, UUID.randomUUID().toString())
    .and(name, "John Doe")
    .and(age, 18)
    .build().execute();
```

### `UPDATE`

```java
// Update John Doe's age to 35
jdbc.update(age, 35)
    .withClauses("WHERE " + name + " = ?")
    .givenClauseParams("John Doe")
    .build().execute();
```

### `DELETE`

```java
// delete ("John Doe", 35)
jdbc.delete(id.getTable())
    .withClauses("WHERE " + name + " = ? AND " + age + " = ?")
    .givenClauseParams("John Doe", 35)
    .build().execute();
```
