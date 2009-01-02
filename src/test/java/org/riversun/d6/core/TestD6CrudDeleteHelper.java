package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.d6.DBConnCreator;
import org.riversun.d6.test.model.School;
import org.riversun.d6.test.model.User;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBDef;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBGen;

/**
 * 
 * Test for D6CrudDeleteHelper class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6CrudDeleteHelper {

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
    public void test_createDeleteAllPreparedSQLStatement() throws Exception {

        final D6CrudDeleteHelper d6CrudDeleteHelper = new D6CrudDeleteHelper(School.class);
        final String expected = "DELETE FROM school";
        final String actual = d6CrudDeleteHelper.createDeleteAllPreparedSQLStatement();

        assertEquals(expected, actual);
    }

    @Test
    public void test_createDeletePreparedSQLStatement() throws Exception {

        final D6CrudDeleteHelper d6CrudDeleteHelper = new D6CrudDeleteHelper(School.class);
        final String expected = "DELETE FROM school WHERE school_id = ?, school_name = ?";
        final String actual = d6CrudDeleteHelper.createDeletePreparedSQLStatement();

        assertEquals(expected, actual);
    }

    @Test
    public void test_map() throws Exception {

        final DBConnCreator dbConnCreator = new DBConnCreator(TestHelper_TestDBDef.createTestDBConnInfo());

        final D6CrudDeleteHelper d6CrudDeleteHelper = new D6CrudDeleteHelper(org.riversun.d6.test.model.User.class);

        final String deleteSQL = d6CrudDeleteHelper.createDeletePreparedSQLStatement();

        PreparedStatement preparedStmt = null;

        final Connection conn = dbConnCreator.createDBConnection();

        conn.setAutoCommit(false);

        User user1 = new User();
        user1.userId = 1;
        user1.userName = "blackman";

        User user2 = new User();
        user2.userId = 2;
        user2.userName = "blueman";

        final User[] usersToDelete = new User[] { user1, user2 };

        preparedStmt = conn.prepareStatement(deleteSQL);

        int deleteCount = 0;
        for (User userToDelete : usersToDelete) {

            d6CrudDeleteHelper.map(userToDelete, preparedStmt);

            // execute SQL
            deleteCount += preparedStmt.executeUpdate();

        }

        // commit finally
        conn.commit();

        assertEquals(2, deleteCount);

    }

}