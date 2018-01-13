package org.saharsh.fluentjdbc.builder;

import java.util.ArrayList;
import java.util.List;

import org.saharsh.fluentjdbc.command.Insert;
import org.springframework.jdbc.core.JdbcTemplate;

public class InsertBuilder extends Builder<Insert> {

    private List<ColumnValue<?>> columnValues = new ArrayList<>();

    public InsertBuilder(JdbcTemplate template) {
        super(template);
    }

    public InsertBuilder insert(ColumnValue<?>... assignments) {
        columnValues = new ArrayList<>();
        for (ColumnValue<?> assignment : assignments) {
            columnValues.add(assignment);
        }
        return this;
    }

    public <T> InsertBuilder and(Column<T> column, T value) {
        columnValues.add(new ColumnValue<T>(column, value));
        return this;
    }

    @Override
    public InsertBuilder withClauses(String clauses) {
        throw new UnsupportedOperationException("Not supported for insert");
    }

    @Override
    public Builder<Insert> givenClauseParams(Object... clauseParams) {
        throw new UnsupportedOperationException("Not supported for insert");
    }

    @Override
    public Insert build() {

        if (columnValues.size() < 1) {
            throw new IllegalArgumentException("No columns provided for insert");
        }

        String table = null;
        String[] columns = new String[columnValues.size()];
        Object[] columnParams = new Object[columnValues.size()];

        for (int i = 0; i < columnValues.size(); i++) {

            ColumnValue<?> assignment = columnValues.get(i);

            Column<?> column = assignment.getColumn();
            Object value = assignment.getValue();

            String _table = column.getTable();
            if (table == null) {
                table = _table;
            } else if (!table.equals(_table)) {
                throw new IllegalArgumentException("Insert only allows one table in command");
            }

            columns[i] = column.getName();
            columnParams[i] = value;
        }
        return new Insert(jdbcTemplate, table, columns, columnParams);
    }

}
