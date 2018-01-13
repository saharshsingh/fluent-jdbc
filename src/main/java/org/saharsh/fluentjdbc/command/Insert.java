package org.saharsh.fluentjdbc.command;

import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

public class Insert extends DmlCommand {

    private final JdbcTemplate template;
    private final String sql;
    private final Object[] params;

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
