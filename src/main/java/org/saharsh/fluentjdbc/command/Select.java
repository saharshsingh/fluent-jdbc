package org.saharsh.fluentjdbc.command;

import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Encapsulates a 'SELECT' command
 *
 * @author Saharsh Singh
 */
public class Select extends JdbcCommand<List<ResultRow>> {

    private final JdbcTemplate jdbcTemplate;
    private final SelectPart<?>[] selectParts;
    private final String sql;
    private final Object[] params;

    /**
     * Constructor
     *
     * @param jdbcTemplate
     *            JDBC Template instance that will be used to run the command
     * @param selectParts
     *            instances of {@link SelectPart} that define query result
     *            columns
     * @param clauses
     *            make up all of the query EXCEPT THE 'SELECT [list of fields]'
     *            part and includes the 'FROM [table]' clause. 'WHERE' clause
     *            should be parameterized
     * @param params
     *            values of the parameters specified in the clauses
     */
    public Select(JdbcTemplate jdbcTemplate, SelectPart<?>[] selectParts, String clauses, Object... params) {

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

    private String buildSql(SelectPart<?>[] selectParts, String clauses) {

        // build SQL string
        StringBuilder sql = new StringBuilder("SELECT ");

        // add columns to select
        boolean notFirstPart = false;
        for (SelectPart<?> selectPart : selectParts) {
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
