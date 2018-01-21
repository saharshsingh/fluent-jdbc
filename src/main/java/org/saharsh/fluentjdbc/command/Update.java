package org.saharsh.fluentjdbc.command;

import java.util.Arrays;

import org.saharsh.fluentjdbc.builder.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * Encapsulates the 'UPDATE' command
 *
 * @author Saharsh Singh
 */
public class Update extends DmlCommand {

    private final JdbcTemplate template;
    private final String sql;
    private final Object[] params;

    /**
     * Constructor
     *
     * @param jdbcTemplate
     *            JDBC Template instance that will be used to run the command
     * @param table
     *            database table to delete from
     * @param columns
     *            {@link Column} instances included in the command. All must
     *            belong to the same database table
     * @param columnParams
     *            values for each of the columns specified in the command. Must
     *            be specified in same order as the columns they need to match
     *            up against
     * @param clauses
     *            parametrized clauses appended to the command
     * @param clauseParams
     *            values of the parameters specified in the clauses
     */
    public Update(JdbcTemplate jdbcTemplate, String table, String[] columns, Object[] columnParams, String clauses,
            Object[] clauseParams) {

        // validate
        if (StringUtils.isEmpty(table)) {
            throw new IllegalArgumentException("table required");
        }
        if (columns == null || columns.length < 1) {
            throw new IllegalArgumentException("Must update atleast one column");
        }
        if (columnParams == null || columnParams.length != columns.length) {
            throw new IllegalArgumentException("Values and Columns length mismatched");
        }

        // template and sql string
        this.template = jdbcTemplate;
        this.sql = buildSql(table, columns, clauses);

        // params injected into sql string
        int paramsLength = columnParams.length;
        if (clauseParams != null) {
            paramsLength += clauseParams.length;
        }
        this.params = new Object[paramsLength];
        int i = 0;
        for (Object columnParam : columnParams) {
            this.params[i++] = columnParam;
        }
        if (clauseParams != null) {
            for (Object clauseParam : clauseParams) {
                this.params[i++] = clauseParam;
            }
        }
    }

    @Override
    public String toString() {
        return "Update [sql=" + sql + ", params=" + Arrays.toString(params) + "]";
    }

    @Override
    protected JdbcTemplate getTemplate() {
        return template;
    }

    @Override
    protected String getSql() {
        return sql;
    }

    @Override
    protected Object[] getParams() {
        return params;
    }

    private String buildSql(String table, String[] columns, String clauses) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(table).append(" SET ");

        boolean notFirstColumn = false;
        for (int i = 0; i < columns.length; i++) {
            if (notFirstColumn) {
                sql.append(" , ");
            }
            sql.append(columns[i]).append(" = ?");
            notFirstColumn = true;
        }

        if (!StringUtils.isEmpty(clauses)) {
            sql.append(" ").append(clauses);
        }

        return sql.toString();
    }

}
