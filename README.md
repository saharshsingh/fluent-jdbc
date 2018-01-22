# Fluent JDBC

## Why?

This library is intended as a simple Fluent API layer on top of Spring Framework's famous [JDBC Template](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#jdbc-JdbcTemplate). It is ideal when you are looking for a lightweight solution to JDBC access from your Java application where a full blown ORM might seem like overkill. This library is not a replacement for an actual ORM. The goals of this library are:

- Provide a Fluent interface powered by JdbcTemplate
- Provide ability to reuse the table and column definitions used in JDBC commands throughout your code. Makes your code more maintainable and less error prone
- Provide an abstraction on top of JdbcTemplate's result mapping interface to avoid repetitive object mapping code.

## How to use?

### FluentJdbc

`org.saharsh.fluentjdbc.FluentJdbc` is the main class you will be interacting with to use this library. It has a `using(JdbcTemplate jdbcTemplate)` you can call to create a new instance. These instances are cheap, assuming the same JdbcTemplate instance is being provided each time, and there's no harm in creating a new one each time you are starting a new command (assuming they will quickly fall out scope afterwards and get garbage collected).

The instance returned by the `using` factory method supports the following four standard SQL commands:

- `SELECT`
- `INSERT`
- `UPDATE`
- `DELETE`

### `SELECT`

```java
Column<String> id = new Column<>("person", "id", String.class);
Column<String> name = new Column<>("person", "name", String.class);
Column<Integer> age = new Column<>("person", "age", Integer.class);
FluentJdbc.using(jdbcTemplate).insert()
```

### `INSERT`

`INSERT` commands work 