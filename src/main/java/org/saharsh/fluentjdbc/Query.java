package org.saharsh.fluentjdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A facade over the {@link JdbcTemplate} API to reduce even more boilerplate
 * and allow for a more fluent interface
 *
 * @author Saharsh Singh
 *
 */
public class Query {

    private final JdbcTemplate jdbcTemplate;
    private SelectPart[] selectParts = null;
    private String clauses = null;
    private Object[] params = new Object[] {};

    /**
     * Register implementation for how to read certain
     * {@link SelectPart#getExpectedType()} from the raw {@link ResultSet}
     * instance returned by the JDBC Driver
     * <p>
     * NOTE: Reader implementations for following types have already been
     * provided. The defaults can be overridden with new implementations. Any
     * types not covered below will need to be added at runtime.
     * <ul>
     * <li>{@link BigDecimal}</li>
     * <li>{@link Boolean}</li>
     * <li>{@link Byte}</li>
     * <li>{@link Double}</li>
     * <li>{@link Float}</li>
     * <li>{@link Integer}</li>
     * <li>{@link Long}</li>
     * <li>{@link Short}</li>
     * <li>{@link String}</li>
     * </ul>
     *
     * @param resultType
     *            field type
     * @param resultReader
     *            reader implementation
     */
    public static void registerResultReader(Class<?> resultType, ResultReader resultReader) {
        QueryResultRowMapper.registerReader(resultType, resultReader);
    }

    static {
        registerResultReader(BigDecimal.class, (part, rs) -> {
            return rs.getBigDecimal(part.getAsLabel());
        });
        registerResultReader(Boolean.class, (part, rs) -> {
            boolean value = rs.getBoolean(part.getAsLabel());
            return rs.wasNull() ? null : Boolean.valueOf(value);
        });
        registerResultReader(Byte.class, (part, rs) -> {
            byte value = rs.getByte(part.getAsLabel());
            return rs.wasNull() ? null : Byte.valueOf(value);
        });
        registerResultReader(Double.class, (part, rs) -> {
            double value = rs.getDouble(part.getAsLabel());
            return rs.wasNull() ? null : Double.valueOf(value);
        });
        registerResultReader(Float.class, (part, rs) -> {
            float value = rs.getFloat(part.getAsLabel());
            return rs.wasNull() ? null : Float.valueOf(value);
        });
        registerResultReader(Integer.class, (part, rs) -> {
            int value = rs.getInt(part.getAsLabel());
            return rs.wasNull() ? null : Integer.valueOf(value);
        });
        registerResultReader(Long.class, (part, rs) -> {
            long value = rs.getLong(part.getAsLabel());
            return rs.wasNull() ? null : Long.valueOf(value);
        });
        registerResultReader(Short.class, (part, rs) -> {
            short value = rs.getShort(part.getAsLabel());
            return rs.wasNull() ? null : Short.valueOf(value);
        });
        registerResultReader(String.class, (part, rs) -> {
            return rs.getString(part.getAsLabel());
        });
    }

    /**
     * @param jdbcTemplate
     * @return new builder instance that can be used to configure a query using
     *         method chaining
     */
    public static Query start(JdbcTemplate jdbcTemplate) {
        return new Query(jdbcTemplate);
    }

    /**
     * Used to specify what the query will select from the database
     *
     * @param selectParts
     *            - All parts of the intended select clause
     * @return builder instance to allow method chaining
     */
    public Query thatSelects(SelectPart... selectParts) {
        if (selectParts == null || selectParts.length < 1) {
            throw new IllegalArgumentException("Must query for atleast one thing");
        }
        this.selectParts = selectParts;
        return this;
    }

    /**
     * Provide the clauses for the query. These clauses make up all of the query
     * EXCEPT THE 'SELECT [list of fields]' part and includes the 'FROM [table]'
     * clause. 'WHERE' clause should contain '?' as placeholder characters and
     * {@link Query#givenParams(Object...)} should be used to configure
     * them
     *
     * @param clauses
     * @return builder instance to allow method chaining
     */
    public Query withClauses(String clauses) {
        if (clauses == null || clauses.length() < 1) {
            throw new IllegalArgumentException("Must provide at least one clause");
        }
        this.clauses = clauses;
        return this;
    }

    /**
     * Provide values for each '?' placeholder in the query
     * 
     * @param params
     * @return builder instance to allow method chaining
     */
    public Query givenParams(Object... params) {
        this.params = params != null ? params : this.params;
        return this;
    }

    /** @return Query results */
    public List<ResultRow> execute() {
        
        if (selectParts == null || clauses == null) {
            throw new IllegalStateException("Query not properly initialized. Provide necessary clauses");
        }

        // build SQL string
        StringBuilder sql = new StringBuilder("SELECT ");

        // add fields to select
        boolean notFirstPart = false;
        for (SelectPart selectPart : selectParts) {
            if (notFirstPart) {
                sql.append(", ");
            }
            notFirstPart = true;

            sql.append(selectPart.getSelectExpression()).append(" ").append(selectPart.getAsLabel());
        }

        // add rest of clause
        sql.append(" ").append(clauses);

        // Create and return query object
        return jdbcTemplate.query(sql.toString(), params, new QueryResultRowMapper(selectParts));
    }

    // intentionally private
    private Query(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
