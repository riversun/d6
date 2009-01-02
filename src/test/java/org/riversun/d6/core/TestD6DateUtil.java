package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 
 * Test for WhereCondition class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6DateUtil {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_getSqlDate_01() throws Exception {

        final java.util.Date nowUtilDate = new java.util.Date();
        final java.sql.Date sqlDate = D6DateUtil.getSqlDate(nowUtilDate);

        assertEquals(nowUtilDate.getTime(), sqlDate.getTime());

    }

    @Test
    public void test_getSqlDate_02() throws Exception {

        final int year = 2015;
        final int month = 7;
        final int date = 8;

        @SuppressWarnings("deprecation")
        final java.util.Date utilDate = new java.util.Date(year - 1900, month - 1, date);
        final java.sql.Date sqlDate = D6DateUtil.getSqlDate(year, month, date);

        assertEquals(utilDate.getTime(), sqlDate.getTime());

    }

    @Test
    public void test_getSqlTime_01() throws Exception {

        final java.util.Date nowUtilDate = new java.util.Date();
        final java.sql.Time sqlTime = D6DateUtil.getSqlTime(nowUtilDate);

        assertEquals(nowUtilDate.getTime(), sqlTime.getTime());

    }

    @Test
    public void test_getSqlTime_02() throws Exception {

        // sql.Time's Date part is 1970-01-01.
        final int year = 1970;
        final int month = 1;
        final int date = 1;

        final int hour = 7;
        final int minute = 58;
        final int second = 00;

        @SuppressWarnings("deprecation")
        final java.util.Date utilDate = new java.util.Date(year - 1900, month - 1, date, hour, minute, second);
        final java.sql.Time sqlTime = D6DateUtil.getSqlTime(hour, minute, second);

        assertEquals(utilDate.getTime(), sqlTime.getTime());

    }

    @Test
    public void test_getSqlTime_03() throws Exception {

        final java.util.Date nowUtilDate = new java.util.Date();
        Timestamp sqlTimeStamp = D6DateUtil.getSqlTimeStamp(nowUtilDate);
        assertEquals(nowUtilDate.getTime(), sqlTimeStamp.getTime());

    }
}