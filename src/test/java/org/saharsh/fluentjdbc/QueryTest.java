package org.saharsh.fluentjdbc;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.saharsh.fluentjdbc.builder.Column;
import org.saharsh.fluentjdbc.command.ResultRow;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryTest {

    private static final Column<String> PERSON_ID = new Column<>("person", "id", String.class);
    private static final Column<String> PERSON_NAME = new Column<>("person", "name", String.class);
    private static final Column<Integer> PERSON_AGE = new Column<>("person", "age", Integer.class);

    private final InMemDB inMemDb = new InMemDB();
    private final JdbcTemplate template = inMemDb.getJdbcTemplate();
    private final FluentJdbc jdbc = FluentJdbc.using(template);

    @After
    public void clean() throws Exception {
        inMemDb.resetTestSchema();
    }

    @Test
    public void test_simple_flow() {

        UUID id = UUID.randomUUID();
        int inserted = jdbc.insert(PERSON_ID, id.toString()).and(PERSON_NAME, "John Doe").and(PERSON_AGE, 30).build()
                .execute();
        Assert.assertEquals(1, inserted);

        List<ResultRow> results = jdbc.select(PERSON_ID, PERSON_NAME, PERSON_AGE).withClauses("FROM PERSON LIMIT 1")
                .build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(id.toString(), results.get(0).get(PERSON_ID));

        int updated = jdbc.update(PERSON_AGE, 31).withClauses("WHERE " + PERSON_ID + " = ?").givenClauseParams(id)
                .build().execute();
        Assert.assertEquals(1, updated);

        results = jdbc.select(PERSON_ID, PERSON_AGE).withClauses("FROM PERSON LIMIT 1").build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(id.toString(), results.get(0).get(PERSON_ID));
        Assert.assertEquals(Integer.valueOf(31), results.get(0).get(PERSON_AGE));

        int deleted = jdbc.delete(PERSON_ID.getTable()).withClauses("WHERE " + PERSON_ID + " = ?").givenClauseParams(id)
                .build().execute();
        Assert.assertEquals(1, deleted);

        results = jdbc.select(PERSON_ID, PERSON_NAME, PERSON_AGE).withClauses("FROM PERSON LIMIT 1").build().execute();
        Assert.assertEquals(0, results.size());
    }

}
