package org.saharsh.fluentjdbc.command;

public abstract class JdbcCommand<T> {

    public T execute() {
        return getExecutionStrategy().execute();
    }

    protected abstract CommandExecution<T> getExecutionStrategy();
}
