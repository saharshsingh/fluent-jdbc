package org.saharsh.fluentjdbc.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Encapsulates one row of the result set that's returned by the database in
 * response to the query
 * 
 * @author Saharsh Singh
 */
public class ResultRow {

    private final Map<SelectPart<?>, Object> resultColumns = new HashMap<>();

    // intentionally package private
    void addColumn(SelectPart<?> selectPart, Object result) {
        if (result != null && !selectPart.getExpectedType().isInstance(result)) {
            throw new RuntimeException("Result type does not match select part");
        }
        resultColumns.put(selectPart, result);
    }

    /**
     * @param resultColumn
     *            {@link SelectPart} instance matching the result column to
     *            fetch
     * @return value returned for specified {@link SelectPart} instance. 'null'
     *         if missing
     */
    @SuppressWarnings("unchecked")
    public <T> T get(SelectPart<T> resultColumn) {
        Object result = resultColumns.get(resultColumn);
        return result == null ? null : (T) result;
    }

    /** @return the entire row as a list of columns */
    public List<Entry<SelectPart<?>, Object>> getAll() {
        List<Entry<SelectPart<?>, Object>> all = new ArrayList<>();
        for (Entry<SelectPart<?>, Object> entry : resultColumns.entrySet()) {
            all.add(entry);
        }
        return all;
    }
}
