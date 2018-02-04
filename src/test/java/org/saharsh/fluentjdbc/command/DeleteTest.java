package org.saharsh.fluentjdbc.command;

import org.junit.Assert;
import org.junit.Test;

public class DeleteTest {

    @Test
    public void test_constructor_when_table_missing() {
        try {
            new Delete(null, null, null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("table required", e.getMessage());
        }
        try {
            new Delete(null, "", null, null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("table required", e.getMessage());
        }
    }

    @Test
    public void test_getSql_for_simple_command() {
        Delete delete = new Delete(null, "table", null, null);
        Assert.assertEquals("delete from table", delete.getSql().toLowerCase());
        String toString = delete.toString();
        Assert.assertTrue(toString.contains(delete.getSql()));
    }

}
