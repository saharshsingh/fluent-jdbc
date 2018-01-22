package org.saharsh.fluentjdbc;

import org.saharsh.fluentjdbc.builder.Column;
import org.saharsh.fluentjdbc.builder.ColumnValue;
import org.saharsh.fluentjdbc.builder.DeleteBuilder;
import org.saharsh.fluentjdbc.builder.InsertBuilder;
import org.saharsh.fluentjdbc.builder.SelectBuilder;
import org.saharsh.fluentjdbc.builder.UpdateBuilder;
import org.saharsh.fluentjdbc.command.Delete;
import org.saharsh.fluentjdbc.command.Insert;
import org.saharsh.fluentjdbc.command.Select;
import org.saharsh.fluentjdbc.command.SelectPart;
import org.saharsh.fluentjdbc.command.Update;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A facade over the {@link JdbcTemplate} API to reduce even more boilerplate
 * and allow for a more fluent interface
 *
 * @author Saharsh Singh
 */
public class FluentJdbc {

    private final JdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate
     *            instance of {@link JdbcTemplate} that all SQL commands will be
     *            dispatched through
     * @return a new instance of this class
     */
    public static FluentJdbc using(JdbcTemplate jdbcTemplate) {
        return new FluentJdbc(jdbcTemplate);
    }

    // intentionally private
    private FluentJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Starts building an {@link Insert} command.
     *
     * @param column
     *            one of the columns to include in the insert command
     * @param value
     *            value to insert for the specified column
     * @return a new instance of {@link InsertBuilder}
     */
    public <T> InsertBuilder insert(Column<T> column, T value) {
        InsertBuilder builder = new InsertBuilder(jdbcTemplate);
        return builder.insert(new ColumnValue<T>(column, value));
    }

    /**
     * Starts building a {@link Select} command.
     * 
     * @param selectParts
     *            instances of {@link SelectPart} that define query result
     *            columns
     * @return a new instance of {@link SelectBuilder}
     */
    public SelectBuilder select(SelectPart<?>... selectParts) {
        SelectBuilder builder = new SelectBuilder(jdbcTemplate);
        return builder.select(selectParts);
    }

    /**
     * Starts building an {@link Update} command.
     * 
     * @param column
     *            one of the columns to include in the update command
     * @param value
     *            value to set for the specified column
     * @return a new instance of {@link UpdateBuilder}
     */
    public <T> UpdateBuilder update(Column<T> column, T value) {
        UpdateBuilder builder = new UpdateBuilder(jdbcTemplate);
        return builder.update(new ColumnValue<T>(column, value));
    }

    /**
     * Starts building a {@link Delete} command
     *
     * @param table
     *            database table to delete from
     * @return a new instance of {@link DeleteBuilder}
     */
    public DeleteBuilder delete(String table) {
        DeleteBuilder builder = new DeleteBuilder(jdbcTemplate);
        return builder.table(table);
    }

}
