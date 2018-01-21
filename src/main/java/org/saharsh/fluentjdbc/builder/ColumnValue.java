package org.saharsh.fluentjdbc.builder;

/**
 * Encapsulates a value assignment to a database table column
 *
 * @author Saharsh Singh
 *
 * @param <T>
 */
public class ColumnValue<T> {

    private final Column<T> column;
    private final T value;

    /**
     * Constructor
     *
     * @param column
     * @param value
     */
    public ColumnValue(Column<T> column, T value) {
        this.column = column;
        this.value = value;
    }

    /** @return {@link Column} instance */
    public Column<T> getColumn() {
        return column;
    }

    /** @return value being assigned to column */
    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ColumnValue [column=" + column + ", value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((column == null) ? 0 : column.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ColumnValue<?> other = (ColumnValue<?>) obj;
        if (column == null) {
            if (other.column != null)
                return false;
        } else if (!column.equals(other.column))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
