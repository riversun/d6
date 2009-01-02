package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.test.model.School;
import org.riversun.d6.test.model.User;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBGen;

/**
 * 
 * Test for D6CrudInsertHelper class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6CrudUpdateHelper {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
        // create test DB(if exist then initialize)
        new TestHelper_TestDBGen().createTestDB();
    }

    @After
    public void tearDown() throws Exception {
        // create test DB(if exist then initialize)
        new TestHelper_TestDBGen().createTestDB();
    }

    @Test
    public void test_getWhereClause_01() throws Exception {

        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(User.class);
        final String expected = "WHERE user_id = ?";
        assertEquals(expected, d6CrudUpdateHelper.getWhereClause());

    }

    @Test
    public void test_getWhereClause_02() throws Exception {

        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(School.class);
        final String expected = "WHERE school_id = ? AND school_name = ?";
        assertEquals(expected, d6CrudUpdateHelper.getWhereClause());

    }

    @Test
    public void test_createUpdatePreparedSQLStatement_01() throws Exception {
        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(User.class);
        String actual = d6CrudUpdateHelper.createUpdatePreparedSQLStatement(null);
        final String expected = "UPDATE user SET user_id = ?, user_name = ?, birth_date = ?, birth_time = ?, married_flag = ?, school_id = ?, created_at = ? WHERE user_id = ?";
        assertEquals(expected, actual);
    }

    @Test
    public void test_createUpdatePreparedSQLStatement_02() throws Exception {
        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(School.class);
        String actual = d6CrudUpdateHelper.createUpdatePreparedSQLStatement(null);
        final String expected = "UPDATE school SET school_id = ?, school_name = ?, school_desc = ? WHERE school_id = ? AND school_name = ?";

        assertEquals(expected, actual);
    }

    @Test
    public void test_createUpdatePreparedSQLStatement_03_with_include_exclude() throws Exception {
        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(User.class);

        D6Inex includeExcludeColumnNames = new D6Inex();
        includeExcludeColumnNames.addExcludeColumn("school_id").addExcludeColumn("married_flag");

        String actual = d6CrudUpdateHelper.createUpdatePreparedSQLStatement(includeExcludeColumnNames);

        final String expected = "UPDATE user SET user_id = ?, user_name = ?, birth_date = ?, birth_time = ?, created_at = ? WHERE user_id = ?";
        assertEquals(expected, actual);
    }

    @Test
    public void test_getPrimaryKeyFieldList() throws Exception {

        D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(User.class);
        List<Field> primaryKeyFieldList = d6CrudUpdateHelper.getPrimaryKeyFieldList();
        Field field = primaryKeyFieldList.get(0);
        DBColumn dbColumn = field.getAnnotation(DBColumn.class);

        assertTrue(dbColumn.isPrimaryKey());

        String expected = "user_id";
        assertEquals(expected, dbColumn.columnName());

    }
}