package org.saharsh.fluentjdbc.command;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a specific part of the select clause that denotes a specific
 * column to be returned in the result set
 *
 * @author Saharsh Singh
 */
public class SelectPart<T> {

    private final String selectExpression;
    private final String asLabel;
    private final Class<T> expectedType;
    private final ResultReader<T> reader;

    /**
     * Constructor
     * <p>
     * NOTE: This constructor assumes the expected type is one of the supported
     * expected types listed below. If the expected type is different, use the
     * {@link SelectPart#SelectPart(String, String, Class, ResultReader)}
     * version to supply the correct {@link ResultReader} implementation.
     * <p>
     * Expected Types supported by default:
     * <ul>
     * <li>{@link BigDecimal}</li>
     * <li>{@link Boolean}</li>
     * <li>{@link Byte}</li>
     * <li>{@link Double}</li>
     * <li>{@link Float}</li>
     * <li>{@link Integer}</li>
     * <li>{@link Long}</li>
     * <li>{@link Short}</li>
     * <li>{@link String}</li>
     * </ul>
     *
     * @param selectExpression
     *            part of select part that tells the database what to query
     *            (e.g. exact column names, operations on a set of columns, etc)
     * @param asLabel
     *            what the returned result column matching this select part
     *            should be labeled or aliased as
     * @param expectedType
     *            The Java type that matches the database type for the value
     *            returned
     */
    @SuppressWarnings("unchecked")
    public SelectPart(String selectExpression, String asLabel, Class<T> expectedType) {
        this(selectExpression, asLabel, expectedType, BUILT_IN_READERS.get(expectedType));
    }

    /**
     * Constructor
     *
     * @param selectExpression
     *            part of select part that tells the database what to query
     *            (e.g. exact column names, operations on a set of columns, etc)
     * @param asLabel
     *            what the returned result column matching this select part
     *            should be labeled or aliased as
     * @param expectedType
     *            The Java type that matches the database type for the value
     *            returned
     * @param reader
     *            provides implementation for reading the expected type from raw
     *            {@link ResultSet} instance
     */
    public SelectPart(String selectExpression, String asLabel, Class<T> expectedType, ResultReader<T> reader) {

        // non null verification for other fields
        if (selectExpression == null || asLabel == null || expectedType == null) {
            throw new IllegalArgumentException("Only non-null args accepted");
        }

        // validate reader
        if (reader == null) {
            throw new IllegalArgumentException("No reader implementation found/provided for " + expectedType);
        }

        this.selectExpression = selectExpression;
        this.asLabel = asLabel;
        this.expectedType = expectedType;
        this.reader = reader;
    }

    /**
     * @return part of select part that tells the database what to query (e.g.
     *         exact column names, operations on a set of columns, etc)
     */
    public String getSelectExpression() {
        return selectExpression;
    }

    /**
     * @return what the returned result column matching this select part should
     *         be labeled or aliased as
     */
    public String getAsLabel() {
        return asLabel;
    }

    /**
     * @return The Java type that matches the database type for the value
     *         returned
     */
    public Class<?> getExpectedType() {
        return expectedType;
    }

    /**
     * @return implementation for reading the expected type from raw
     *         {@link ResultSet} instance
     */
    public ResultReader<T> getReader() {
        return reader;
    }

    @Override
    public String toString() {
        return getSelectExpression();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + asLabel.hashCode();
        result = prime * result + expectedType.hashCode();
        result = prime * result + selectExpression.hashCode();
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
        SelectPart<?> other = (SelectPart<?>) obj;
        if (!selectExpression.equals(other.selectExpression))
            return false;
        if (!asLabel.equals(other.asLabel))
            return false;
        if (!expectedType.equals(other.expectedType))
            return false;
        return true;
    }

    // BUILT-IN READERS
    @SuppressWarnings("rawtypes")
    private static final Map<Class, ResultReader> BUILT_IN_READERS = new HashMap<>();
    static {
        BUILT_IN_READERS.put(BigDecimal.class, (part, rs) -> {
            return rs.getBigDecimal(part.getAsLabel());
        });
        BUILT_IN_READERS.put(Boolean.class, (part, rs) -> {
            boolean value = rs.getBoolean(part.getAsLabel());
            return rs.wasNull() ? null : Boolean.valueOf(value);
        });
        BUILT_IN_READERS.put(Byte.class, (part, rs) -> {
            byte value = rs.getByte(part.getAsLabel());
            return rs.wasNull() ? null : Byte.valueOf(value);
        });
        BUILT_IN_READERS.put(Double.class, (part, rs) -> {
            double value = rs.getDouble(part.getAsLabel());
            return rs.wasNull() ? null : Double.valueOf(value);
        });
        BUILT_IN_READERS.put(Float.class, (part, rs) -> {
            float value = rs.getFloat(part.getAsLabel());
            return rs.wasNull() ? null : Float.valueOf(value);
        });
        BUILT_IN_READERS.put(Integer.class, (part, rs) -> {
            int value = rs.getInt(part.getAsLabel());
            return rs.wasNull() ? null : Integer.valueOf(value);
        });
        BUILT_IN_READERS.put(Long.class, (part, rs) -> {
            long value = rs.getLong(part.getAsLabel());
            return rs.wasNull() ? null : Long.valueOf(value);
        });
        BUILT_IN_READERS.put(Short.class, (part, rs) -> {
            short value = rs.getShort(part.getAsLabel());
            return rs.wasNull() ? null : Short.valueOf(value);
        });
        BUILT_IN_READERS.put(String.class, (part, rs) -> {
            return rs.getString(part.getAsLabel());
        });
    }

}
