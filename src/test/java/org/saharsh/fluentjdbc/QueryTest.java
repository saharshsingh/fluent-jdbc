package org.saharsh.fluentjdbc;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryTest {

    private static final Field<String> PERSON_ID = new Field<>("person", "id", String.class);
    private static final Field<String> PERSON_NAME = new Field<>("person", "name", String.class);
    private static final Field<Integer> PERSON_AGE = new Field<>("person", "age", Integer.class);

    private final InMemDB inMemDb = new InMemDB();
    private final JdbcTemplate template = inMemDb.getJdbcTemplate();

    @Before
    public void setup() throws Exception {
        template.execute("INSERT INTO PERSON (ID, NAME, AGE) VALUES ('" + UUID.randomUUID() + "', 'John Doe', 30)");
    }

    @Test
    public void test_simple_select() {
        List<ResultRow> results = Query.withTemplate(template).thatSelects(PERSON_NAME, PERSON_AGE)
                .withClauses("FROM PERSON LIMIT 1").execute();
        Assert.assertEquals(1, results.size());
    }

    @After
    public void clean() throws Exception {
        inMemDb.resetTestSchema();
    }


}
