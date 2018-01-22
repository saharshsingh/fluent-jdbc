package org.saharsh.fluentjdbc.builder;

import org.junit.Assert;
import org.junit.Test;

public class InsertBuilderTest {

    @Test
    public void test_with_clauses_not_supported() {
        InsertBuilder builder = new InsertBuilder(null);
        try {
            builder.withClauses("blah");
            Assert.fail("Expected exception");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not supported for insert", e.getMessage());
        }
    }

    @Test
    public void test_given_params_not_supported() {
        InsertBuilder builder = new InsertBuilder(null);
        try {
            builder.givenClauseParams("blah", "blah", "blah");
            Assert.fail("Expected exception");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not supported for insert", e.getMessage());
        }
    }

    @Test
    public void test_case_when_no_columns_provided() {
        InsertBuilder builder = new InsertBuilder(null);
        try {
            builder.build();
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No columns provided for insert", e.getMessage());
        }
    }

    @Test
    public void test_case_when_columns_from_different_tables_provided() {
        InsertBuilder builder = new InsertBuilder(null);
        builder.insert(new ColumnValue<String>(new Column<String>("table", "column", String.class), "some value"),
                new ColumnValue<String>(new Column<String>("another_table", "another_column", String.class),
                        "another value"));
        try {
            builder.build();
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Insert only allows one table in command", e.getMessage());
        }
    }

}
