package org.saharsh.fluentjdbc.command;

@FunctionalInterface
public interface CommandExecution<T> {
    T execute();
}
