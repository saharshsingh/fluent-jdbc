package org.saharsh.fluentjdbc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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

    private static final Column<String> CLASS_ID = new Column<>("class", "id", String.class);
    private static final Column<String> CLASS_NAME = new Column<>("class", "name", String.class);
    private static final Column<String> CLASS_TEACHER = new Column<>("class", "teacher", String.class);
    private static final Column<Boolean> CLASS_ACTIVE = new Column<>("class", "active", Boolean.class);
    private static final Column<Double> CLASS_AVERAGE_GRADE = new Column<>("class", "average_grade", Double.class);

    private static final Column<String> GRADE_ID = new Column<>("grade", "id", String.class);
    private static final Column<Double> GRADE_GRADE = new Column<>("grade", "grade", Double.class);
    private static final Column<String> GRADE_CLASS_ID = new Column<>("grade", "class_id", String.class);
    private static final Column<String> GRADE_STUDENT_ID = new Column<>("grade", "student_id", String.class);
    private static final Column<Date> GRADE_DATE_CREATED = new Column<>("grade", "date_created", Date.class,
            (part, rs) -> {
                return rs.getTimestamp(part.getAsLabel());
            });

    private final InMemDB inMemDb = InMemDB.get();
    private final JdbcTemplate template = inMemDb.getJdbcTemplate();
    private final FluentJdbc jdbc = FluentJdbc.using(template);

    private Date testStart = null;
    private String studentId = null;
    private String classId = null;
    private String gradeId = null;

    @Before
    public void setup() throws Exception {

        testStart = new Timestamp(System.currentTimeMillis());

        inMemDb.createTestSchema();

        // create student
        studentId = UUID.randomUUID().toString();
        int inserted = jdbc.insert(STUDENT_ID, studentId).and(STUDENT_NAME, "John Doe").and(STUDENT_AGE, 30).build()
                .execute();
        Assert.assertEquals(1, inserted);

        // create class
        classId = UUID.randomUUID().toString();
        inserted = jdbc.insert(CLASS_ID, classId).and(CLASS_NAME, "Computer Science 101")
                .and(CLASS_TEACHER, "Dr. Jane Doe").and(CLASS_ACTIVE, Boolean.TRUE).and(CLASS_AVERAGE_GRADE, 77.5D)
                .build().execute();
        Assert.assertEquals(1, inserted);

        // create grade
        gradeId = UUID.randomUUID().toString();
        inserted = jdbc.insert(GRADE_ID, gradeId).and(GRADE_GRADE, 93.5D).and(GRADE_CLASS_ID, classId)
                .and(GRADE_STUDENT_ID, studentId).build().execute();
        Assert.assertEquals(1, inserted);
    }

    @After
    public void clean() throws Exception {
        inMemDb.dropTestSchema();
    }

    @Test
    public void test_selecting_and_updating_student() {

        List<ResultRow> results = jdbc.select(STUDENT_ID, STUDENT_NAME, STUDENT_AGE)
                .withClauses("FROM " + STUDENT_ID.getTable() + " LIMIT 1").build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(studentId, results.get(0).get(STUDENT_ID));
        Assert.assertEquals("John Doe", results.get(0).get(STUDENT_NAME));
        Assert.assertEquals(Integer.valueOf(30), results.get(0).get(STUDENT_AGE));

        int updated = jdbc.update(STUDENT_AGE, null).and(STUDENT_NAME, "Jenny Doe")
                .withClauses("WHERE " + STUDENT_ID + " = ?").givenClauseParams(studentId).build().execute();
        Assert.assertEquals(1, updated);

        results = jdbc.select(STUDENT_ID, STUDENT_NAME, STUDENT_AGE)
                .withClauses("FROM " + STUDENT_ID.getTable() + " LIMIT 1").build().execute();
        Assert.assertEquals(1, results.size());
        List<Entry<SelectPart<?>, Object>> resultColumns = results.get(0).getAll();
        Assert.assertEquals(3, resultColumns.size());
        for (Entry<SelectPart<?>, Object> resultColumn : resultColumns) {
            Assert.assertEquals(resultColumn.getValue(), results.get(0).get(resultColumn.getKey()));
        }
        Assert.assertEquals(studentId, results.get(0).get(STUDENT_ID));
        Assert.assertEquals("Jenny Doe", results.get(0).get(STUDENT_NAME));
        Assert.assertEquals(null, results.get(0).get(STUDENT_AGE));
    }

    @Test
    public void test_selecting_and_updating_class() {

        List<ResultRow> results = jdbc.select(CLASS_ID, CLASS_NAME, CLASS_TEACHER, CLASS_ACTIVE, CLASS_AVERAGE_GRADE)
                .withClauses("FROM " + CLASS_ID.getTable() + " LIMIT 1").build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(classId, results.get(0).get(CLASS_ID));
        Assert.assertEquals("Computer Science 101", results.get(0).get(CLASS_NAME));
        Assert.assertEquals("Dr. Jane Doe", results.get(0).get(CLASS_TEACHER));
        Assert.assertEquals(Boolean.TRUE, results.get(0).get(CLASS_ACTIVE));
        Assert.assertEquals(Double.valueOf(77.5D), results.get(0).get(CLASS_AVERAGE_GRADE));

        int updated = jdbc.update(CLASS_ACTIVE, null).and(CLASS_AVERAGE_GRADE, null)
                .withClauses("WHERE " + CLASS_ID + " = ?").givenClauseParams(classId).build().execute();
        Assert.assertEquals(1, updated);

        results = jdbc.select(CLASS_ID, CLASS_NAME, CLASS_ACTIVE, CLASS_AVERAGE_GRADE)
                .withClauses("FROM " + CLASS_ID.getTable()).build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(classId, results.get(0).get(CLASS_ID));
        Assert.assertEquals("Computer Science 101", results.get(0).get(CLASS_NAME));
        Assert.assertEquals(null, results.get(0).get(CLASS_TEACHER));
        Assert.assertEquals(null, results.get(0).get(CLASS_ACTIVE));
        Assert.assertEquals(null, results.get(0).get(CLASS_AVERAGE_GRADE));
    }

    @Test
    public void test_selecting_updating_and_deleting_grade() {

        // select
        List<ResultRow> results = jdbc
                .select(GRADE_ID, GRADE_GRADE, GRADE_DATE_CREATED, GRADE_CLASS_ID, GRADE_STUDENT_ID)
                .withClauses("FROM " + GRADE_ID.getTable()).build().execute();
        Assert.assertEquals(1, results.size());

        // verify fields
        Assert.assertEquals(gradeId, results.get(0).get(GRADE_ID));
        Assert.assertEquals(Double.valueOf(93.5D), results.get(0).get(GRADE_GRADE));
        Assert.assertEquals(classId, results.get(0).get(GRADE_CLASS_ID));
        Assert.assertEquals(studentId, results.get(0).get(GRADE_STUDENT_ID));

        // verify date created
        Date dateCreated = results.get(0).get(GRADE_DATE_CREATED);
        Assert.assertNotNull(dateCreated);
        Assert.assertFalse(dateCreated.before(testStart));
        Assert.assertFalse(dateCreated.after(new Timestamp(System.currentTimeMillis())));

        // delete grade
        Assert.assertEquals(Integer.valueOf(1), jdbc.delete(GRADE_ID.getTable())
                .withClauses("WHERE " + GRADE_ID + " = ?").givenClauseParams(gradeId).build().execute());
        Assert.assertEquals(0,
                jdbc.select(GRADE_ID).withClauses("FROM " + GRADE_ID.getTable()).build().execute().size());
    }

    @Test
    public void test_casting_between_different_integer_types() {

        // byte
        Column<Byte> ageByte = new Column<>("student", "age", Byte.class);
        List<ResultRow> results = jdbc.select(ageByte).withClauses("FROM " + ageByte.getTable()).build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Byte.valueOf((byte) 30), results.get(0).get(ageByte));

        // long
        Column<Long> ageLong = new Column<>("student", "age", Long.class);
        results = jdbc.select(ageLong).withClauses("FROM " + ageLong.getTable()).build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Long.valueOf(30L), results.get(0).get(ageLong));

        // long
        Column<Short> ageShort = new Column<>("student", "age", Short.class);
        results = jdbc.select(ageShort).withClauses("FROM " + ageShort.getTable()).build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Short.valueOf((short) 30), results.get(0).get(ageShort));

        // set value to null
        Assert.assertEquals(Integer.valueOf(1), jdbc.update(ageShort, null).build().execute());
        Assert.assertEquals(null,
                jdbc.select(ageByte).withClauses("FROM " + ageByte.getTable()).build().execute().get(0).get(ageByte));
        Assert.assertEquals(null,
                jdbc.select(ageLong).withClauses("FROM " + ageLong.getTable()).build().execute().get(0).get(ageLong));
        Assert.assertEquals(null, jdbc.select(ageShort).withClauses("FROM " + ageShort.getTable()).build().execute()
                .get(0).get(ageShort));
    }

    @Test
    public void test_casting_between_different_decimal_types() {

        // float
        Column<Float> avgGradeFloat = new Column<>("class", "average_grade", Float.class);
        List<ResultRow> results = jdbc.select(avgGradeFloat).withClauses("FROM " + avgGradeFloat.getTable()).build()
                .execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(Float.valueOf(77.5F), results.get(0).get(avgGradeFloat));

        // float
        Column<BigDecimal> avgGradeBigD = new Column<>("class", "average_grade", BigDecimal.class);
        results = jdbc.select(avgGradeBigD).withClauses("FROM " + avgGradeBigD.getTable()).build().execute();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(BigDecimal.valueOf(77.5F), results.get(0).get(avgGradeBigD));

        // set value to null
        Assert.assertEquals(Integer.valueOf(1), jdbc.update(avgGradeFloat, null).build().execute());
        Assert.assertEquals(null, jdbc.select(avgGradeFloat).withClauses("FROM " + avgGradeFloat.getTable()).build()
                .execute().get(0).get(avgGradeFloat));
        Assert.assertEquals(null, jdbc.select(avgGradeBigD).withClauses("FROM " + avgGradeBigD.getTable()).build()
                .execute().get(0).get(avgGradeBigD));
    }

}
