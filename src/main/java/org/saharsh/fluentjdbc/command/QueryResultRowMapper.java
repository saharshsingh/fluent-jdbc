package org.saharsh.fluentjdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

// intentionally package private
final class QueryResultRowMapper implements RowMapper<ResultRow> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, ResultReader> READERS = new HashMap<>();

    static <T> void registerReader(Class<T> type, ResultReader<T> reader) {
        READERS.put(type, reader);
    }

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
            ResultReader extractor = READERS.get(part.getExpectedType());
            if (extractor == null) {
                throw new RuntimeException("Unsupported result type: " + part.getExpectedType().getName());
            }
            row.addColumn(part, extractor.read(part, resultSet));
        }
        return row;
    }

}
