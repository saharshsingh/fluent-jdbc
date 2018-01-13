package org.saharsh.fluentjdbc.builder;

import org.saharsh.fluentjdbc.command.SelectPart;

/**
 * Represents a column or column in a database table
 *
 * @author Saharsh Singh
 *
 * @param <T>
 *            - Java type that should match returned data
 */
public class Column<T> extends SelectPart {

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

    /** @return name of table this column belongs to */
    public String getTable() {
        return table;
    }

    /** @return column name */
    public String getName() {
        return name;
    }

    /** @return table.name */
    public String getQName() {
        return getSelectExpression();
    }

}
