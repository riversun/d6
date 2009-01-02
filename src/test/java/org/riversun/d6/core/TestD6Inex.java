package org.riversun.d6.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;

/**
 * 
 * Test for D6Inex class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6Inex {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_addIncludeColumn() throws Exception {
        D6Inex o = new D6Inex();

        // begin of preparing source data========================
        final Set<String> columnNameSet = new LinkedHashSet<String>();
        final String[] columnNameDataSource = new String[] {
                "user_id",
                "user_name",
                "birth_date",
                "birth_time",
                "married_flag",
                "school_id",
                "created_at",
        };
        for (String columnNameData : columnNameDataSource) {
            columnNameSet.add(columnNameData);
        }
        // end of preparing source data========================

        o.addIncludeColumn("user_id");
        o.addIncludeColumn("user_name");
        o.manipulate(columnNameSet);

        final String[] expectedDataSource = new String[] {
                "user_id",
                "user_name",
        };

        for (String expectedData : expectedDataSource) {
            assertTrue(columnNameSet.contains(expectedData));
        }

        final String[] notExpectedDataSource = new String[] {
                "birth_date",
                "birth_time",
                "married_flag",
                "school_id",
                "created_at",
        };
        for (String notExpectedData : notExpectedDataSource) {
            assertFalse(columnNameSet.contains(notExpectedData));
        }

    }

    @Test
    public void test_addExcludeColumn() throws Exception {
        D6Inex o = new D6Inex();

        // begin of preparing source data========================
        final Set<String> columnNameSet = new LinkedHashSet<String>();
        final String[] columnNameDataSource = new String[] {
                "user_id",
                "user_name",
                "birth_date",
                "birth_time",
                "married_flag",
                "school_id",
                "created_at",
        };
        for (String columnNameData : columnNameDataSource) {
            columnNameSet.add(columnNameData);
        }
        // end of preparing source data========================

        o.addExcludeColumn("birth_time");
        o.addExcludeColumn("married_flag");
        o.manipulate(columnNameSet);

        final String[] expectedDataSource = new String[] {
                "user_id",
                "user_name",
                "birth_date",
                "school_id",
                "created_at",
        };

        for (String expectedData : expectedDataSource) {
            assertTrue(columnNameSet.contains(expectedData));
        }

        final String[] notExpectedDataSource = new String[] {
                "birth_time",
                "married_flag",
        };
        for (String notExpectedData : notExpectedDataSource) {
            assertFalse(columnNameSet.contains(notExpectedData));
        }

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_exception() throws Exception {
        D6Inex o = new D6Inex();

        o.addIncludeColumn("birth_time");
        o.addExcludeColumn("married_flag");

        // begin of preparing source data========================
        final Set<String> columnNameSet = new LinkedHashSet<String>();
        final String[] columnNameDataSource = new String[] {
                "user_id",
                "user_name",
                "birth_date",
                "birth_time",
                "married_flag",
                "school_id",
                "created_at",
        };
        for (String columnNameData : columnNameDataSource) {
            columnNameSet.add(columnNameData);
        }

        expectedException.expect(RuntimeException.class);

        // end of preparing source data========================
        o.manipulate(columnNameSet);

    }
}