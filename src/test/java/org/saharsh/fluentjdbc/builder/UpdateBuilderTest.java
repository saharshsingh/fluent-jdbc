package org.saharsh.fluentjdbc.builder;

import org.junit.Assert;
import org.junit.Test;

public class UpdateBuilderTest {

    @Test
    public void test_case_when_no_columns_provided() {
        UpdateBuilder builder = new UpdateBuilder(null);
        try {
            builder.build();
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No columns provided for update", e.getMessage());
        }
    }

    @Test
    public void test_case_when_columns_from_different_tables_provided() {
        UpdateBuilder builder = new UpdateBuilder(null);
        builder.update(new ColumnValue<String>(new Column<String>("table", "column", String.class), "some value"),
                new ColumnValue<String>(new Column<String>("another_table", "another_column", String.class),
                        "another value"));
        try {
            builder.build();
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Update only allows one table in command", e.getMessage());
        }
    }

}
