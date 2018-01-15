package org.saharsh.fluentjdbc;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.saharsh.fluentjdbc.builder.Column;
import org.saharsh.fluentjdbc.command.ResultRow;
import org.saharsh.fluentjdbc.command.SelectPart;
import org.springframework.jdbc.core.JdbcTemplate;

public class SanityCheckTest {

    private static final Column<String> STUDENT_ID = new Column<>("student", "id", String.class);
    private static final Column<String> STUDENT_NAME = new Column<>("student", "name", String.class);
    private static final Column<Integer> STUDENT_AGE = new Column<>("student", "age", Integer.class);

    private final InMemDB inMemDb = InMemDB.get();
    private final JdbcTemplate template = inMemDb.getJdbcTemplate();
    private final FluentJdbc jdbc = FluentJdbc.using(template);

    @Before
    public void setup() throws Exception {
        inMemDb.createTestSchema();
    }

    @After
    public void clean() throws Exception {
        inMemDb.dropTestSchema();
    }

    @Test
    public void test_simple_flow() {

        UUID id = UUID.randomUUID();
        int inserted = jdbc.insert(STUDENT_ID, id.toString()).and(STUDENT_NAME, "John Doe").and(STUDENT_AGE, 30).build()
                .execute();
        Assert.assertEquals(1, inserted);

        List<ResultRow> results = jdbc.select(STUDENT_ID, STUDENT_NAME, STUDENT_AGE)
                .withClauses("FROM " + STUDENT_ID.getTable() + " LIMIT 1")
                .build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(id.toString(), results.get(0).get(STUDENT_ID));
        Assert.assertEquals("John Doe", results.get(0).get(STUDENT_NAME));
        Assert.assertEquals(Integer.valueOf(30), results.get(0).get(STUDENT_AGE));

        int updated = jdbc.update(STUDENT_AGE, 31).and(STUDENT_NAME, "Jenny Doe")
                .withClauses("WHERE " + STUDENT_ID + " = ?").givenClauseParams(id).build().execute();
        Assert.assertEquals(1, updated);

        results = jdbc.select(STUDENT_ID, STUDENT_NAME, STUDENT_AGE)
                .withClauses("FROM " + STUDENT_ID.getTable() + " LIMIT 1").build().execute();
        Assert.assertEquals(1, results.size());
        List<Entry<SelectPart<?>, Object>> resultColumns = results.get(0).getAll();
        Assert.assertEquals(3, resultColumns.size());
        for (Entry<SelectPart<?>, Object> resultColumn : resultColumns) {
            Assert.assertEquals(resultColumn.getValue(), results.get(0).get(resultColumn.getKey()));
        }
        Assert.assertEquals(id.toString(), results.get(0).get(STUDENT_ID));
        Assert.assertEquals("Jenny Doe", results.get(0).get(STUDENT_NAME));
        Assert.assertEquals(Integer.valueOf(31), results.get(0).get(STUDENT_AGE));

        int deleted = jdbc.delete(STUDENT_ID.getTable()).withClauses("WHERE " + STUDENT_ID + " = ?").givenClauseParams(id)
                .build().execute();
        Assert.assertEquals(1, deleted);

        results = jdbc.select(STUDENT_ID, STUDENT_NAME, STUDENT_AGE)
                .withClauses("FROM " + STUDENT_ID.getTable() + " LIMIT 1").build().execute();
        Assert.assertEquals(0, results.size());
    }

}
