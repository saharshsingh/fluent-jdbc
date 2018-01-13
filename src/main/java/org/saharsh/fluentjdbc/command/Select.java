package org.saharsh.fluentjdbc.command;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class Select extends JdbcCommand<List<ResultRow>> {

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
     *            column type
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

    private final JdbcTemplate jdbcTemplate;
    private final SelectPart[] selectParts;
    private final String sql;
    private final Object[] params;

    public Select(JdbcTemplate jdbcTemplate, SelectPart[] selectParts, String clauses, Object... params) {

        // validate
        if (selectParts == null || selectParts.length < 1) {
            throw new IllegalArgumentException("Must query for atleast one thing");
        }
        if (clauses == null || clauses.length() < 1) {
            throw new IllegalArgumentException("Must provide at least one clause");
        }

        // assign
        this.jdbcTemplate = jdbcTemplate;
        this.selectParts = selectParts;
        this.sql = buildSql(selectParts, clauses);
        this.params = params;
    }

    @Override
    public String toString() {
        return "Select [sql=" + sql + ", params=" + Arrays.toString(params) + "]";
    }

    @Override
    protected CommandExecution<List<ResultRow>> getExecutionStrategy() {
        return () -> {
            return jdbcTemplate.query(sql, params, new QueryResultRowMapper(selectParts));
        };
    }

    private String buildSql(SelectPart[] selectParts, String clauses) {

        // build SQL string
        StringBuilder sql = new StringBuilder("SELECT ");

        // add columns to select
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

        return sql.toString();
    }

}
