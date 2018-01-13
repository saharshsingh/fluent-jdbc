package org.saharsh.fluentjdbc.command;

import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

public class Delete extends DmlCommand {

    private final JdbcTemplate template;
    private final String sql;
    private final Object[] params;

    public Delete(JdbcTemplate jdbcTemplate, String table, String clauses, Object[] clauseParams) {

        // validate
        if (StringUtils.isEmpty(table)) {
            throw new IllegalArgumentException("table required");
        }

        // jdbctemplate and sql string
        this.template = jdbcTemplate;
        this.sql = buildSql(table, clauses);
        this.params = clauseParams;
    }

    @Override
    public String toString() {
        return "Delete [sql=" + sql + ", params=" + Arrays.toString(params) + "]";
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

    private String buildSql(String table, String clauses) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table);

        if (!StringUtils.isEmpty(clauses)) {
            sql.append(" ").append(clauses);
        }

        return sql.toString();
    }

}
