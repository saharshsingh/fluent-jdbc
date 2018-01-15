package org.saharsh.fluentjdbc.command;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.saharsh.fluentjdbc.FluentJdbc;
import org.saharsh.fluentjdbc.InMemDB;
import org.saharsh.fluentjdbc.builder.Column;
import org.springframework.jdbc.core.JdbcTemplate;

public class InsertAndSelectTest {

    private static final Column<String> STUDENT_ID = new Column<>("student", "id", String.class);
    private static final Column<String> STUDENT_NAME = new Column<>("student", "name", String.class);
    private static final Column<Integer> STUDENT_AGE = new Column<>("student", "age", Integer.class);

    private static final Column<String> CLASS_ID = new Column<>("class", "id", String.class);
    private static final Column<String> CLASS_NAME = new Column<>("class", "name", String.class);
    private static final Column<String> CLASS_TEACHER = new Column<>("class", "teacher", String.class);
    private static final Column<Boolean> CLASS_ACTIVE = new Column<>("class", "active", Boolean.class);

    private static final Column<String> GRADE_ID = new Column<>("GRADE", "ID", String.class);
    private static final Column<Double> GRADE = new Column<>("GRADE", "GRADE", Double.class);
    private static final Column<String> GRADE_CLASS_ID = new Column<>("GRADE", "CLASS_ID", String.class);
    private static final Column<String> GRADE_STUDENT_ID = new Column<>("GRADE", "STUDENT_ID", String.class);

    private final InMemDB inMemDb = InMemDB.get();
    private final JdbcTemplate template = inMemDb.getJdbcTemplate();
    private final FluentJdbc jdbc = FluentJdbc.using(template);

    @After
    public void clean() throws Exception {
        inMemDb.dropTestSchema();
    }

    @Test
    public void test_simple_join() {

        String studentId = UUID.randomUUID().toString();
        String classId = UUID.randomUUID().toString();
        String gradeId = UUID.randomUUID().toString();

        Assert.assertEquals(Integer.valueOf(1), jdbc.insert(STUDENT_ID, studentId).and(STUDENT_NAME, "John Doe")
                .and(STUDENT_AGE, 15).build().execute());
        Assert.assertEquals(Integer.valueOf(1), jdbc.insert(CLASS_ID, classId).and(CLASS_NAME, "Comp Sci 101")
                .and(CLASS_TEACHER, "Dr. Brilliant").and(CLASS_ACTIVE, Boolean.TRUE).build().execute());
        Assert.assertEquals(Integer.valueOf(1), jdbc.insert(GRADE_ID, gradeId).and(GRADE, 84.7D)
                .and(GRADE_CLASS_ID, classId).and(GRADE_STUDENT_ID, studentId).build().execute());

        List<ResultRow> results = jdbc.select(STUDENT_NAME, CLASS_NAME, GRADE)
                .withClauses("FROM " + GRADE.getTable() + 
                        " JOIN " + STUDENT_NAME.getTable() + " ON " + STUDENT_ID + " = " + GRADE_STUDENT_ID + 
                        " JOIN " + CLASS_NAME.getTable() + " ON " + CLASS_ID + " = " + GRADE_CLASS_ID)
                .build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("John Doe", results.get(0).get(STUDENT_NAME));
        Assert.assertEquals("Comp Sci 101", results.get(0).get(CLASS_NAME));
        Assert.assertEquals(Double.valueOf(84.7D), results.get(0).get(GRADE));
    }

}
