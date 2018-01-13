package org.saharsh.fluentjdbc;

import org.saharsh.fluentjdbc.builder.Column;
import org.saharsh.fluentjdbc.builder.ColumnValue;
import org.saharsh.fluentjdbc.builder.DeleteBuilder;
import org.saharsh.fluentjdbc.builder.InsertBuilder;
import org.saharsh.fluentjdbc.builder.SelectBuilder;
import org.saharsh.fluentjdbc.builder.UpdateBuilder;
import org.saharsh.fluentjdbc.command.SelectPart;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A facade over the {@link JdbcTemplate} API to reduce even more boilerplate
 * and allow for a more fluent interface
 *
 * @author Saharsh Singh
 *
 */
public class FluentJdbc {

    private final JdbcTemplate jdbcTemplate;

    public static FluentJdbc using(JdbcTemplate jdbcTemplate) {
        return new FluentJdbc(jdbcTemplate);
    }

    private FluentJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> InsertBuilder insert(Column<T> column, T value) {
        InsertBuilder builder = new InsertBuilder(jdbcTemplate);
        return builder.insert(new ColumnValue<T>(column, value));
    }

    public SelectBuilder select(SelectPart... selectParts) {
        SelectBuilder builder = new SelectBuilder(jdbcTemplate);
        return builder.select(selectParts);
    }

    public <T> UpdateBuilder update(Column<T> column, T value) {
        UpdateBuilder builder = new UpdateBuilder(jdbcTemplate);
        return builder.update(new ColumnValue<T>(column, value));
    }

    public DeleteBuilder delete(String table) {
        DeleteBuilder builder = new DeleteBuilder(jdbcTemplate);
        return builder.table(table);
    }

}
