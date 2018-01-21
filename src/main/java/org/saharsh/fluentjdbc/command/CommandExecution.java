package org.saharsh.fluentjdbc.command;

/**
 * Instead of having subclasses override {@link JdbcCommand#execute()}, they are
 * forced to supply the execution logic via an instance of this functional
 * interface.
 *
 * @author Saharsh Singh
 *
 * @param <T>
 *            type that encapsulates the result of the command
 */
@FunctionalInterface
public interface CommandExecution<T> {

    /**
     * Execute a concrete {@link JdbcCommand} instance
     *
     * @return result(s) of the executed command
     */
    T execute();

}
