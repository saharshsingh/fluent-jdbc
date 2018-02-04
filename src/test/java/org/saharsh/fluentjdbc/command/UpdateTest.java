package org.saharsh.fluentjdbc.command;

import org.junit.Assert;
import org.junit.Test;

public class UpdateTest {

    @Test
    public void test_constructor_when_table_missing() {
        try {
            new Update(null, null, null, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("table required", e.getMessage());
        }
        try {
            new Update(null, "", null, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("table required", e.getMessage());
        }
    }

    @Test
    public void test_constructor_when_columns_missing() {
        try {
            new Update(null, "table", null, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must update atleast one column", e.getMessage());
        }
        try {
            new Update(null, "table", new String[] {}, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must update atleast one column", e.getMessage());
        }
    }

    @Test
    public void test_constructor_when_column_params_malformed() {

        String[] columns = new String[] { "col_one", "col_two" };

        try {
            new Update(null, "table", columns, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Values and Columns length mismatched", e.getMessage());
        }
        try {
            new Update(null, "table", columns, new Object[] { Integer.valueOf(5), "some_value", "another_value" }, null,
                    null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Values and Columns length mismatched", e.getMessage());
        }
    }

    @Test
    public void test_getSql_for_simple_command() {
        String[] columns = new String[] { "col_one", "col_two" };
        Object[] params = new Object[] { Integer.valueOf(5), "some_value" };
        Update command = new Update(null, "table", columns, params, null, null);
        Assert.assertEquals("update table set col_one = ? , col_two = ?", command.getSql().toLowerCase());
        Assert.assertTrue(command.toString().contains(command.getSql()));
    }

}
