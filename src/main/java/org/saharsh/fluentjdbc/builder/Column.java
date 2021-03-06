package org.saharsh.fluentjdbc.builder;

import java.sql.ResultSet;

import org.saharsh.fluentjdbc.command.ResultReader;
import org.saharsh.fluentjdbc.command.SelectPart;

/**
 * Represents a column or column in a database table
 *
 * @author Saharsh Singh
 *
 * @param <T>
 *            - Java type that should match returned data
 */
public class Column<T> extends SelectPart<T> {

    private final String table;
    private final String name;

    /**
     * Constructor
     *
     * @param table
     * @param name
     * @param type
     *            - Java type that should match returned data
     */
    public Column(String table, String name, Class<T> type) {
        super(table + "." + name, table + "_" + name, type);
        this.table = table;
        this.name = name;
    }

    /**
     * Constructor
     *
     * @param table
     * @param name
     * @param type
     *            - Java type that should match returned data
     * @param reader
     *            provides implementation for reading the expected type from raw
     *            {@link ResultSet} instance
     */
    public Column(String table, String name, Class<T> type, ResultReader<T> reader) {
        super(table + "." + name, table + "_" + name, type, reader);
        this.table = table;
        this.name = name;
    }

    /** @return name of table this column belongs to */
    public String getTable() {
        return table;
    }

    /** @return column name */
    public String getName() {
        return name;
    }

}
