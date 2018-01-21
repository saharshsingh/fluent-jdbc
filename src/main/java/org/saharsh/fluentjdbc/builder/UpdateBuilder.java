package org.saharsh.fluentjdbc.builder;

import java.util.ArrayList;
import java.util.List;

import org.saharsh.fluentjdbc.command.Update;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Builder for {@link Update} commands
 *
 * @author Saharsh Singh
 */
public class UpdateBuilder extends Builder<Update> {

    private List<ColumnValue<?>> columnValues = new ArrayList<>();

    /**
     * Constructor
     *
     * @param jdbcTemplate
     *            JDBC Template instance that will be used to run the command
     */
    public UpdateBuilder(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * Provide multiple column=value pairs to be included in the command.
     * <p>
     * NOTE: This method overwrites any mappings that were provided previously
     *
     * @param assignments
     * @return instance of this builder to allow method chaining
     */
    public UpdateBuilder update(ColumnValue<?>... assignments) {
        columnValues = new ArrayList<>();
        for (ColumnValue<?> assignment : assignments) {
            columnValues.add(assignment);
        }
        return this;
    }

    /**
     * Provide another column-value pair to be included in this command
     *
     * @param column
     * @param value
     * @return instance of this builder to allow method chaining
     */
    public <T> UpdateBuilder and(Column<T> column, T value) {
        columnValues.add(new ColumnValue<T>(column, value));
        return this;
    }

    @Override
    public Update build() {

        if (columnValues.size() < 1) {
            throw new IllegalArgumentException("No columns provided for update");
        }

        String table = null;
        String[] columns = new String[columnValues.size()];
        Object[] columnParams = new Object[columnValues.size()];

        for (int i = 0; i < columnValues.size(); i++) {

            ColumnValue<?> columnValue = columnValues.get(i);

            Column<?> column = columnValue.getColumn();
            Object value = columnValue.getValue();

            String _table = column.getTable();
            if (table == null) {
                table = _table;
            } else if (!table.equals(_table)) {
                throw new IllegalArgumentException("Update only allows one table in command");
            }

            columns[i] = column.getSelectExpression();
            columnParams[i] = value;
        }
        return new Update(jdbcTemplate, table, columns, columnParams, clauses, clauseParams);
    }

}
