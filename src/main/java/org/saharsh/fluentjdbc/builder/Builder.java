package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.JdbcCommand;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Base class for all {@link JdbcCommand} builder implementations
 * 
 * @author Saharsh Singh
 *
 * @param <T>
 *            the subclass of {@link JdbcCommand} this builder is used to build
 *            an instance of
 */
public abstract class Builder<T extends JdbcCommand<?>> {

    protected final JdbcTemplate jdbcTemplate;
    protected String clauses;
    protected Object[] clauseParams;

    /**
     * Constructor
     *
     * @param jdbcTemplate
     *            JDBC Template instance that will be used to run the command
     */
    public Builder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Provide the clauses for the query. These are appended as is to the end of
     * the SQL command string that is sent to the database
     *
     * @param clauses
     *            parametrized clauses appended to the command
     * @return instance of this builder to allow method chaining
     */
    public Builder<T> withClauses(String clauses) {
        this.clauses = clauses;
        return this;
    }

    /**
     * Provide values for each '?' placeholder in the query
     *
     * @param clauseParams
     * @return instance of this builder to allow method chaining
     */
    public Builder<T> givenClauseParams(Object... clauseParams) {
        this.clauseParams = clauseParams;
        return this;
    }

    /**
     * @return instance of {@link JdbcCommand} built using state of this builder
     */
    public abstract T build();

}
