package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 
 * Test for CrudSelectHelper class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6CrudSelectHelper {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_general() throws Exception {

        final D6CrudSelectHelper d6CrudSelectHelper = new D6CrudSelectHelper(org.riversun.d6.test.model.User.class);
        final String expected = "SELECT COUNT(user_id) FROM user";
        final String actual = d6CrudSelectHelper.getSQLForSelectCount();

        assertEquals(expected, actual);

    }

}