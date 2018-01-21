package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.Select;
import org.saharsh.fluentjdbc.command.SelectPart;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Builder for {@link Select} commands
 *
 * @author Saharsh Singh
 */
public class SelectBuilder extends Builder<Select> {

    private SelectPart<?>[] selectParts;

    /**
     * Constructor
     *
     * @param jdbcTemplate
     *            JDBC Template instance that will be used to run the command
     */
    public SelectBuilder(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Used to specify what the query will select from the database
     *
     * @param selectParts
     *            - All parts of the intended select clause
     * @return builder instance to allow method chaining
     */
    public SelectBuilder select(SelectPart<?>... selectParts) {
        this.selectParts = selectParts;
        return this;
    }

    @Override
    public Select build() {
        return new Select(jdbcTemplate, selectParts, clauses, clauseParams);
    }
}
