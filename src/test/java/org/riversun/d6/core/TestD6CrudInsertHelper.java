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
import org.riversun.d6.test.model.User;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBDef;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBGen;

/**
 * 
 * Test for D6CrudInsertHelper class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6CrudInsertHelper {

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
    public void test_createInsertPreparedSQLStatement() throws Exception {

        D6CrudInsertHelper D6CrudInsertHelper = new D6CrudInsertHelper(org.riversun.d6.test.model.User.class);

        final String expected = "INSERT INTO user (user_name, birth_date, birth_time, married_flag, school_id, created_at) VALUES ( ?, ?, ?, ?, ?, ? )";

        assertEquals(expected, D6CrudInsertHelper.createInsertPreparedSQLStatement());

    }

    @Test
    public void test_map() throws Exception {

        final DBConnCreator dbConnCreator = new DBConnCreator(TestHelper_TestDBDef.createTestDBConnInfo());

        final D6CrudInsertHelper d6CrudInsertHelper = new D6CrudInsertHelper(org.riversun.d6.test.model.User.class);

        final String insertSQL = d6CrudInsertHelper.createInsertPreparedSQLStatement();

        PreparedStatement preparedStmt = null;

        Connection conn = dbConnCreator.createDBConnection();

        conn.setAutoCommit(false);

        User user1 = new User();
        user1.userId = 10;// primary key must not be reflect
        user1.userName = "Kepler";

        User user2 = new User();
        user2.userId = 20;// primery key must not be reflect
        user2.userName = "Ganymede";
        user2.marriedFlag = true;

        final User[] users = new User[] { user1, user2 };

        preparedStmt = conn.prepareStatement(insertSQL);

        int updateCount = 0;
        for (User user : users) {

            // map user object to DB(preparedStmt)
            D6Inex includeExcludeColumnNames = null;

            d6CrudInsertHelper.map(user, preparedStmt, includeExcludeColumnNames);

            // execute SQL
            updateCount += preparedStmt.executeUpdate();

        }

        // commit finally
        conn.commit();

        assertEquals(2, updateCount);

    }
}