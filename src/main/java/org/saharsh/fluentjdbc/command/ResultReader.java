package org.saharsh.fluentjdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements how to read a raw {@link ResultSet} instance that maps to the
 * given {@link SelectPart} instance
 *
 * @author Saharsh Singh
 */
@FunctionalInterface
public interface ResultReader {

    /**
     * Implements how to read a raw {@link ResultSet} instance that maps to the
     * given {@link SelectPart} instance
     *
     * @param selectPart
     * @param resultSet
     * @return Java Object extracted or created from the {@link ResultSet}
     *         instance
     * @throws SQLException
     */
    Object read(SelectPart selectPart, ResultSet resultSet) throws SQLException;

}