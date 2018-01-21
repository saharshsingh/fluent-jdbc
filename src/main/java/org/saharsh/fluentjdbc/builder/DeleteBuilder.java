package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.Delete;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Builder for {@link Delete} commands
 *
 * @author Saharsh Singh
 */
public class DeleteBuilder extends Builder<Delete> {

    private String table = null;

    /**
     * Constructor
     *
     * @param template
     *            JDBC Template instance that will be used to run the command
     */
    public DeleteBuilder(JdbcTemplate template) {
        super(template);
    }

    /**
     * Provide the database table that the command will delete from
     *
     * @param table
     * @return instance of this builder to allow method chaining
     */
    public DeleteBuilder table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public Delete build() {
        return new Delete(jdbcTemplate, table, clauses, clauseParams);
    }

}
