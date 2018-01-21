package org.saharsh.fluentjdbc.command;

/**
 * Base class for all JDBC Commands
 *
 * @author Saharsh Singh
 *
 * @param <T>
 *            type that encapsulates the result of the command
 */
public abstract class JdbcCommand<T> {

    /**
     * Execute this command
     *
     * @return result(s) of this command
     */
    public final T execute() {
        return getExecutionStrategy().execute();
    }

    /**
     * @return Closure supplied by this method is executed by
     *         {@link JdbcCommand#execute()}
     */
    protected abstract CommandExecution<T> getExecutionStrategy();

}
