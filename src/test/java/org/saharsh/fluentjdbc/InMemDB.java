package org.saharsh.fluentjdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class InMemDB {

    private static InMemDB INSTANCE = null;

    private final JdbcTemplate jdbcTemplate;

    public static InMemDB get() {
        if (INSTANCE == null) {
            synchronized (InMemDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InMemDB();
                }
            }
        }
        return INSTANCE;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void createTestSchema() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS STUDENT ( " +
                "ID CHAR(36), " +
                "NAME VARCHAR(255) NOT NULL, " +
                "AGE INTEGER, " +
                "PRIMARY KEY (ID) )");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CLASS ( " +
                "ID CHAR(36), " +
                "NAME VARCHAR(255) NOT NULL, " +
                "TEACHER VARCHAR(255) NOT NULL, " +
                "ACTIVE BOOLEAN DEFAULT FALSE, " +
                "AVERAGE_GRADE DECIMAL, " +
                "PRIMARY KEY (ID) )");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS GRADE ( " +
                "ID CHAR(36), " +
                "GRADE DECIMAL NOT NULL, " +
                "DATE_CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "CLASS_ID CHAR(36) NOT NULL, " +
                "STUDENT_ID CHAR(36) NOT NULL, " +
                "PRIMARY KEY (ID), " +
                "FOREIGN KEY (CLASS_ID) REFERENCES CLASS(ID), " +
                "FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID) )");
    }

    public void dropTestSchema() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS GRADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS CLASS");
        jdbcTemplate.execute("DROP TABLE IF EXISTS STUDENT");
    }

    // intentionally private
    private InMemDB() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbcTemplate = new JdbcTemplate(ds);
        createTestSchema();
    }

}
