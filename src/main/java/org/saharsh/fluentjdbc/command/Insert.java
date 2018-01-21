package org.saharsh.fluentjdbc.command;

import java.util.Arrays;

import org.saharsh.fluentjdbc.builder.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * Encapsulates the 'INSERT' SQL command
 *
 * @author Saharsh Singh
 */
public class Insert extends DmlCommand {

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
     */
    public Insert(JdbcTemplate jdbcTemplate, String table, String[] columns, Object[] columnParams) {

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
        this.sql = buildSql(table, columns);
        this.params = columnParams;
    }

    @Override
    public String toString() {
        return "Insert [sql=" + sql + ", params=" + Arrays.toString(params) + "]";
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

    private String buildSql(String table, String[] columns) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(table).append(" (");

        StringBuilder valuesPart = new StringBuilder(" VALUES (");
        boolean notFirstColumn = false;
        for (String column : columns) {
            if (notFirstColumn) {
                sql.append(" , ");
                valuesPart.append(" , ");
            }
            sql.append(column);
            valuesPart.append("?");
            notFirstColumn = true;
        }

        valuesPart.append(")");
        sql.append(") ").append(valuesPart.toString());

        return sql.toString();
    }

}
