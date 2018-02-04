package org.saharsh.fluentjdbc.command;

import org.junit.Assert;
import org.junit.Test;

public class QueryResultRowMapperTest {

    @Test
    public void test_constructor_validation() {
        try {
            new QueryResultRowMapper((SelectPart[]) null);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No select parts identified", e.getMessage());
        }
        try {
            new QueryResultRowMapper(new SelectPart[] {});
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No select parts identified", e.getMessage());
        }
    }

}
