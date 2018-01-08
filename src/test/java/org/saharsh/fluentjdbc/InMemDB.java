package org.saharsh.fluentjdbc;

import java.util.concurrent.atomic.AtomicReference;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class InMemDB {

    private final AtomicReference<JdbcTemplate> jdbcTemplateRef = new AtomicReference<>();

    public JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplateRef.get() == null) {
            synchronized (jdbcTemplateRef) {
                if (jdbcTemplateRef.get() == null) {

                    // Create JdbcTemplate powered by in-mem H2 database
                    JdbcDataSource ds = new JdbcDataSource();
                    ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
                    jdbcTemplateRef.set(new JdbcTemplate(ds));
                    InMemDB.intTestSchema(jdbcTemplateRef.get());
                }
            }
        }
        return jdbcTemplateRef.get();
    }

    public void resetTestSchema() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("DROP TABLE IF EXISTS PERSON");
        InMemDB.intTestSchema(template);
    }

    private static void intTestSchema(JdbcTemplate template) {
        template.execute("CREATE TABLE PERSON ( ID CHAR(36), NAME VARCHAR(255) NOT NULL, "
                + "AGE INTEGER NOT NULL, PRIMARY KEY (ID) )");
    }

}
