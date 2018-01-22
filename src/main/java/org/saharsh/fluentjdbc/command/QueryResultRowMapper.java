package org.saharsh.fluentjdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

// intentionally package private
final class QueryResultRowMapper implements RowMapper<ResultRow> {

    private final List<SelectPart<?>> expectedParts = new ArrayList<>();

    QueryResultRowMapper(SelectPart<?>... parts) {
        if (parts == null || parts.length < 1) {
            throw new IllegalArgumentException("No select parts identified");
        }
        for (SelectPart<?> part : parts) {
            expectedParts.add(part);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ResultRow mapRow(ResultSet resultSet, int index) throws SQLException {
        ResultRow row = new ResultRow();
        for (SelectPart part : expectedParts) {
            ResultReader reader = part.getReader();
            row.addColumn(part, reader.read(part, resultSet));
        }
        return row;
    }

}
