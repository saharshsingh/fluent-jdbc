package org.saharsh.fluentjdbc.command;

import org.junit.Assert;
import org.junit.Test;

public class SelectTest {

    @Test
    public void test_constructor_for_missing_select_parts() {
        try {
            new Select(null, null, null);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must query for atleast one thing", e.getMessage());
        }
        try {
            new Select(null, new SelectPart[] {}, null, (Object[]) null);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must query for atleast one thing", e.getMessage());
        }
    }

    @Test
    public void test_constructor_for_missing_clause() {
        try {
            new Select(null, new SelectPart[] { new SelectPart<>("blah", "blah", String.class) }, null);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must provide nonempty clause", e.getMessage());
        }
        try {
            new Select(null, new SelectPart[] { new SelectPart<>("blah", "blah", String.class) }, "");
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must provide nonempty clause", e.getMessage());
        }
    }

    @Test
    public void test_toString() {
        SelectPart<?>[] select = new SelectPart[] { new SelectPart<>("blah", "blah", String.class) };
        String toString = new Select(null, select, "from table").toString();
        Assert.assertTrue(toString.toLowerCase().contains("select blah blah from table"));
    }

}
