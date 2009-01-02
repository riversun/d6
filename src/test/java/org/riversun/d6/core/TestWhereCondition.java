package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import java.sql.Time;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;

/**
 * 
 * Test for WhereCondition class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestWhereCondition {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    public void method() {

        WhereCondition w1 = new WhereCondition().Col("user_id").Equals().Val("?").AND().Col("user_name").Equals().Val("bluemane");
        WhereCondition w2 = new WhereCondition().Col("aid").Equals().Val("?").AND().Col("bid").Equals().Val("bluemane");
        // w1.AND();
        // w1.WHERE(w2);

        WhereCondition w = new WhereCondition();
        w.WHERE(w1).AND().WHERE(w2);
    }

    @Test
    public void test_whereCondition_00_basic() throws Exception {

        // basic where SQL
        final int userId = 1;
        final WhereCondition whereCondition = new WhereCondition().Col("user_id").Equals().Val(userId);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE user_id='1'";

        assertEquals(expectedSQL, generatedSQL);
    }

    @Test
    public void test_whereCondition_01_basic_use_AND() throws Exception {

        // use 'AND' condition
        final int userId = 1;
        final String userName = "blackman";
        final WhereCondition whereCondition = new WhereCondition().Col("user_id").Equals().Val(userId).AND().Col("user_name").Equals().Val(userName);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE user_id='1' AND user_name='blackman'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_02_basic_use_OR() throws Exception {

        // use 'OR' condition
        final int userId1 = 1;
        final int userId2 = 2;

        final WhereCondition whereCondition = new WhereCondition().Col("user_id").Equals().Val(userId1).OR().Col("user_id").Equals().Val(userId2);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE user_id='1' OR user_id='2'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_03_basic_use_WILD_CARD() throws Exception {

        // use wild card for prepared statement
        final WhereCondition whereCondition = new WhereCondition().Col("user_id").Equals().Val("?");

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE user_id= ?";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_03_basic_use_WILD_CARD_02() throws Exception {

        // use wild card for prepared statement
        final WhereCondition whereCondition = new WhereCondition().Col("user_id").Equals().ValWildCard();

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE user_id= ?";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_10_operator_more_than() throws Exception {

        // uses operator '>'

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").MoreThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time>'13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_11_operator_more_equals_than() throws Exception {

        // uses operator '>='

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").MoreEqualsThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time>='13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_12_operator_less_than() throws Exception {

        // uses operator '<'

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").LessThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time<'13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_13_operator_less_than() throws Exception {

        // uses operator '<'

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").LessThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time<'13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_14_operator_less_equals_than() throws Exception {

        // uses operator '<='

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").LessEqualsThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time<='13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_20_condition_less_equals_than() throws Exception {

        // uses operator '<='

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").LessEqualsThan().Val(time);

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time<='13:00:10'";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_30_composit_00() throws Exception {

        // composit = packaged where block

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").MoreThan().Val(time).AND().WHERE(new WhereCondition().Col("school_id").Equals().Val(1));

        final String generatedSQL = whereCondition.toSql();
        final String expectedSQL = "WHERE birth_time>'13:00:10' AND (school_id='1')";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Test
    public void test_whereCondition_31_composit_01() throws Exception {

        // composit = packaged where block

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition1 = new WhereCondition().Col("birth_time").MoreThan().Val(time).OR().Col("married_flag").Equals().Val(1);
        final WhereCondition whereCondition2 = new WhereCondition().Col("school_id").Equals().Val(1);

        final WhereCondition compositWhereCondition = new WhereCondition();
        compositWhereCondition.WHERE(whereCondition1).AND().WHERE(whereCondition2);

        final String generatedSQL = compositWhereCondition.toSql();
        final String expectedSQL = "WHERE (birth_time>'13:00:10' OR married_flag='1') AND (school_id='1')";

        assertEquals(expectedSQL, generatedSQL);

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_whereCondition_90_syntax_error_01() throws Exception {

        // check the where condition's method order is incorrect.

        expectedException.expect(D6RuntimeException.class);

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").Val(time);

    }

    @Test
    public void test_whereCondition_91_syntax_error_02() throws Exception {

        // check the where condition's method order is incorrect.

        expectedException.expect(D6RuntimeException.class);

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Col("birth_time").Equals().Val(time).Val(time);

    }

    @Test
    public void test_whereCondition_92_syntax_error_03() throws Exception {

        // check the where condition's method order is incorrect.
        expectedException.expect(D6RuntimeException.class);

        final java.sql.Time time = new Time(13, 00, 10);
        final WhereCondition whereCondition = new WhereCondition().Val(time);

    }

    @Test
    public void test_whereCondition_93_syntax_error_04() throws Exception {

        // Statement ends with half-baked state
        expectedException.expect(D6RuntimeException.class);

        final java.sql.Time time = new Time(13, 00, 10);
        final String SQL = new WhereCondition().Col("birth_time").Equals().Val(time).AND().toSql();

    }

}