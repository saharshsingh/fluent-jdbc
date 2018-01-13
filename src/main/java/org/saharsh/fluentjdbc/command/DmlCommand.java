package org.saharsh.fluentjdbc.command;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DmlCommand extends JdbcCommand<Integer> {

    protected abstract JdbcTemplate getTemplate();

    protected abstract String getSql();

    protected abstract Object[] getParams();

    @Override
    protected CommandExecution<Integer> getExecutionStrategy() {
        return () -> {
            return getTemplate().update(getSql(), getParams());
        };
    }

}
