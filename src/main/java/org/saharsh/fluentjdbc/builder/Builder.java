package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.JdbcCommand;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class Builder<T extends JdbcCommand<?>> {

    protected final JdbcTemplate jdbcTemplate;
    protected String clauses;
    protected Object[] clauseParams;

    public Builder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Builder<T> withClauses(String clauses) {
        this.clauses = clauses;
        return this;
    }

    public Builder<T> givenClauseParams(Object... clauseParams) {
        this.clauseParams = clauseParams != null ? clauseParams : this.clauseParams;
        return this;
    }

    public abstract T build();

}
