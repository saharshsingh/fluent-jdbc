package org.saharsh.fluentjdbc.command;

import org.junit.Assert;
import org.junit.Test;

public class ResultRowTest {

    @Test
    public void test_addColumn_for_mismatched_types() {
        try {
            new ResultRow().addColumn(new SelectPart<>("blah", "blah", String.class), Integer.valueOf(5));
            Assert.fail("Expected exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("Result type does not match select part", e.getMessage());
        }
    }

}
