package org.saharsh.fluentjdbc.command;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Base class for Data Manipulation Language (DML) commands
 *
 * @author Saharsh Singh
 */
public abstract class DmlCommand extends JdbcCommand<Integer> {

    /** @return {@link JdbcTemplate} instance that will be used to execute */
    protected abstract JdbcTemplate getTemplate();

    /** @return The parameterized SQL statement */
    protected abstract String getSql();

    /** @return values for parameters specified in the SQL statement */
    protected abstract Object[] getParams();

    /**
     * @return Default DML command execution implementation. Sends the SQL
     *         statement and parameters to
     *         {@link JdbcTemplate#update(String, Object...)}
     */
    @Override
    protected CommandExecution<Integer> getExecutionStrategy() {
        return () -> {
            return getTemplate().update(getSql(), getParams());
        };
    }

}
