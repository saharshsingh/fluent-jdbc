package org.saharsh.fluentjdbc.command;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class SelectPartTest {

    @Test
    public void test_constructor_to_reject_null_args() {
        try {
            new SelectPart<>(null, "ASLabel", String.class);
            Assert.fail("Should've thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Only non-null args accepted", e.getMessage());
        }
        try {
            new SelectPart<>("SelectExpr", null, String.class);
            Assert.fail("Should've thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Only non-null args accepted", e.getMessage());
        }
        try {
            new SelectPart<>("SelectExpr", "ASLabel", null);
            Assert.fail("Should've thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Only non-null args accepted", e.getMessage());
        }
    }

    @Test
    public void test_constructor_to_reject_return_type_without_reader() {
        try {
            new SelectPart<>("SelectExpr", "ASLabel", Date.class);
            Assert.fail("Should've thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No reader implementation found/provided for " + Date.class, e.getMessage());
        }
        try {
            new SelectPart<>("SelectExpr", "ASLabel", Date.class, null);
            Assert.fail("Should've thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No reader implementation found/provided for " + Date.class, e.getMessage());
        }
    }

    @Test
    public void test_as_map_key() {
        Map<SelectPart<String>, String> testMap = new HashMap<>();

        testMap.put(new SelectPart<>("table.column", "Column", String.class), "value");
        Assert.assertEquals("value", testMap.get(new SelectPart<>("table.column", "Column", String.class)));
    }

    @Test
    public void test_equals() {
        SelectPart<String> same1 = new SelectPart<>("count(*)", "Size", String.class);
        SelectPart<String> same2 = new SelectPart<>("count(*)", "Size", String.class);
        SelectPart<String> another = new SelectPart<>("person.age", "Age", String.class);
        SelectPart<String> yetAnother = new SelectPart<>("person.age", "Person Age", String.class);
        SelectPart<Integer> stillAnother = new SelectPart<>("person.age", "Person Age", Integer.class);

        Assert.assertTrue(same1.equals(same1));
        Assert.assertTrue(same1.equals(same2));

        Assert.assertFalse(same1.equals(another));
        Assert.assertFalse(another.equals(yetAnother));
        Assert.assertFalse(yetAnother.equals(stillAnother));

        Assert.assertFalse(same1.equals(null));
        Assert.assertFalse(same1.equals("String"));
    }

}
