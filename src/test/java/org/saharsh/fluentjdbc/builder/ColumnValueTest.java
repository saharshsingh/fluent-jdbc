package org.saharsh.fluentjdbc.builder;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ColumnValueTest {

    @Test
    public void test_as_map_key() {
        Map<ColumnValue<String>, String> testMap = new HashMap<>();

        testMap.put(new ColumnValue<String>(new Column<>("table", "column", String.class), "value"), "value");
        Assert.assertEquals("value",
                testMap.get(new ColumnValue<String>(new Column<>("table", "column", String.class), "value")));

        testMap.put(new ColumnValue<String>(null, "value2"), "value2");
        Assert.assertEquals("value2", testMap.get(new ColumnValue<String>(null, "value2")));

        testMap.put(new ColumnValue<String>(new Column<>("table", "column", String.class), null), "value3");
        Assert.assertEquals("value3",
                testMap.get(new ColumnValue<String>(new Column<>("table", "column", String.class), null)));
    }

    @Test
    public void test_equals() {

        // Define two instances of the same column
        Column<String> sameColumn1 = new Column<>("some_table", "some_column", String.class);
        Column<String> sameColumn2 = new Column<>("some_table", "some_column", String.class);

        // Define some other columns
        Column<String> anotherColumn = new Column<>("some_table", "another_column", String.class);
        Column<String> yetAnotherColumn = new Column<>("another_table", "some_column", String.class);

        // verify
        // same column instance
        Assert.assertEquals(new ColumnValue<String>(sameColumn1, "Value"),
                new ColumnValue<String>(sameColumn1, "Value"));
        Assert.assertNotEquals(new ColumnValue<String>(sameColumn1, "AnotherValue"),
                new ColumnValue<String>(sameColumn1, "Value"));

        // different but equivalent column instances
        Assert.assertEquals(new ColumnValue<String>(sameColumn1, "Value"),
                new ColumnValue<String>(sameColumn2, "Value"));
        Assert.assertNotEquals(new ColumnValue<String>(sameColumn1, "AnotherValue"),
                new ColumnValue<String>(sameColumn2, "Value"));

        // different and unequal column instances
        Assert.assertNotEquals(new ColumnValue<String>(anotherColumn, "Value"),
                new ColumnValue<String>(yetAnotherColumn, "Value"));
        Assert.assertNotEquals(new ColumnValue<String>(sameColumn1, "Value"),
                new ColumnValue<String>(anotherColumn, "Value"));

        // edge cases
        ColumnValue<String> cv = new ColumnValue<String>(sameColumn1, "Value");
        Assert.assertTrue(cv.equals(cv));
        Assert.assertFalse(cv.equals(null));
        Assert.assertFalse(cv.equals(sameColumn1));

        ColumnValue<String> nullColumn = new ColumnValue<String>(null, "Value");
        ColumnValue<String> nullValue = new ColumnValue<String>(sameColumn1, null);
        Assert.assertFalse(nullColumn.equals(cv));
        Assert.assertTrue(new ColumnValue<String>(null, "Value").equals(nullColumn));
        Assert.assertFalse(nullValue.equals(cv));
        Assert.assertTrue(new ColumnValue<String>(sameColumn1, null).equals(nullValue));
    }

}
