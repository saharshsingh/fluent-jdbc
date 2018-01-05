package org.saharsh.fluentjdbc;

/**
 * Represents a field or column in a database table
 *
 * @author Saharsh Singh
 *
 * @param <T>
 *            - Java type that should match returned data
 */
public class Field<T> extends SelectPart {

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
    public Field(String table, String name, Class<T> type) {
        super(table + "." + name, table + "_" + name, type);
        this.table = table;
        this.name = name;
    }

    /** @return name of table this field belongs to */
    public String getTable() {
        return table;
    }

    /** @return field name */
    public String getName() {
        return name;
    }

    /** @return table.name */
    public String getQName() {
        return getSelectExpression();
    }

}
