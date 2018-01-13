package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.Delete;
import org.springframework.jdbc.core.JdbcTemplate;

public class DeleteBuilder extends Builder<Delete> {

    private String table = null;

    public DeleteBuilder(JdbcTemplate template) {
        super(template);
    }

    public DeleteBuilder table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public Delete build() {
        return new Delete(jdbcTemplate, table, clauses, clauseParams);
    }

}
