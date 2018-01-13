package org.saharsh.fluentjdbc.command;

/**
 * Encapsulates a specific part of the select clause that denotes a specific
 * column to be returned in the result set
 *
 * @author Saharsh Singh
 */
public class SelectPart {

    private final String selectExpression;
    private final String asLabel;
    private final Class<?> expectedType;

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
     */
    public SelectPart(String selectExpression, String asLabel, Class<?> expectedType) {
        if (selectExpression == null || asLabel == null || expectedType == null) {
            throw new IllegalArgumentException("Only non-null args accepted");
        }
        this.selectExpression = selectExpression;
        this.asLabel = asLabel;
        this.expectedType = expectedType;
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

    @Override
    public String toString() {
        return getSelectExpression();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((asLabel == null) ? 0 : asLabel.hashCode());
        result = prime * result + ((expectedType == null) ? 0 : expectedType.hashCode());
        result = prime * result + ((selectExpression == null) ? 0 : selectExpression.hashCode());
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
        SelectPart other = (SelectPart) obj;
        if (asLabel == null) {
            if (other.asLabel != null)
                return false;
        } else if (!asLabel.equals(other.asLabel))
            return false;
        if (expectedType == null) {
            if (other.expectedType != null)
                return false;
        } else if (!expectedType.equals(other.expectedType))
            return false;
        if (selectExpression == null) {
            if (other.selectExpression != null)
                return false;
        } else if (!selectExpression.equals(other.selectExpression))
            return false;
        return true;
    }

}
