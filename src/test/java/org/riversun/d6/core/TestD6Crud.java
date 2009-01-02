package org.riversun.d6.core;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.d6.DBConnInfo;
import org.riversun.d6.test.model.School;
import org.riversun.d6.test.model.User;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBDef;
import org.riversun.d6.test.prepare_test.TestHelper_TestDBGen;

/**
 * Test for D6Crud class <br>
 * <br>
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6Crud {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
        createDB();
    }

    @After
    public void tearDown() throws Exception {
        createDB();
    }

    private void createDB() {
        // create test DB(if exist then initialize)
        new TestHelper_TestDBGen().createTestDB();
    }

    private D6Crud createCrud() {
        final DBConnInfo dbConnInfo = TestHelper_TestDBDef.createTestDBConnInfo();
        final D6Crud crud = new D6Crud(dbConnInfo);
        return crud;
    }

    // TEST DELETE ==================================================

    // TEST DELETE ==========================================================
    @Test
    public void test_delete_1_delete_one() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        User[] users = (User[]) crud.execSelectTable(User.class);

        User user2del = users[0];

        int countBefore = crud.execSelectCount(User.class);
        boolean success = crud.execDelete(user2del);
        int countAfter = crud.execSelectCount(User.class);

        User[] usersAfter = (User[]) crud.execSelectTable(User.class);

        assertTrue(success);
        assertEquals(countAfter, countBefore - 1);
        assertEquals((Integer) 2, usersAfter[0].userId);

    }

    @Test
    public void test_delete_2_delete_multi() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        User[] users = (User[]) crud.execSelectTable(User.class);

        int countBefore = crud.execSelectCount(User.class);
        boolean success = crud.execDelete(new User[] { users[0], users[1] });
        int countAfter = crud.execSelectCount(User.class);

        User[] usersAfter = (User[]) crud.execSelectTable(User.class);

        assertTrue(success);
        assertEquals(countAfter, countBefore - 2);
        assertEquals((Integer) 3, usersAfter[0].userId);

    }

    @Test
    public void test_delete_3_delete_all_elements() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        boolean success = crud.execDelete(User.class);
        int countAfter = crud.execSelectCount(User.class);

        assertTrue(success);
        assertEquals(countAfter, 0);

    }

    // TEST UPDATE ==========================================================
    @Test
    public void test_update_1() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        // before
        User[] usersBefore = (User[]) crud.execSelectTable(User.class);
        User userToUpdate = usersBefore[0];

        // change username
        userToUpdate.userName = "orangeman";

        // execute update
        crud.execUpdate(userToUpdate);

        // after
        User[] usersAfter = (User[]) crud.execSelectTable(User.class);
        User userAfter = usersAfter[0];

        assertEquals("orangeman", userAfter.userName);

    }

    @Test
    public void test_update_2_width_exclude_option() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        // before
        User[] usersBefore = (User[]) crud.execSelectTable(User.class);
        User userToUpdate = usersBefore[0];

        // check marriegedFlag is true now,
        assertTrue(userToUpdate.marriedFlag);

        // change marriegedFlag to false,here.
        userToUpdate.marriedFlag = false;

        D6Inex includeExcludeColumnNames = new D6Inex();
        includeExcludeColumnNames.addExcludeColumn("married_flag");

        // execute update
        crud.execUpdate(userToUpdate, includeExcludeColumnNames);

        // after
        User[] usersAfter = (User[]) crud.execSelectTable(User.class);
        User userAfter = usersAfter[0];

        // check marriegedFlag hasn't been changed,because married_flag was
        // marked as excluded.
        assertTrue(userAfter.marriedFlag);

    }

    // update SQL for free SQL format
    @Test
    public void test_execUpdateByRawSQL_10_update_statement() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        final String RAW_SQL = "UPDATE user SET school_id=0";

        crud.execUpdateByRawSQL(RAW_SQL);

        User[] users = (User[]) crud.execSelectTable(User.class);

        final Integer expectedSchoolId = 0;

        for (int i = 0; i < users.length; i++) {
            assertEquals(expectedSchoolId, users[i].schoolId);
        }

    }

    @Test
    public void test_execUpdateByRawSQL_20_delete_statement() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        final String RAW_SQL = "DELETE FROM user WHERE married_flag=1";

        crud.execUpdateByRawSQL(RAW_SQL);

        User[] users = (User[]) crud.execSelectTable(User.class);

        assertEquals((Integer) 2, users[0].userId);
        assertEquals((Integer) 5, users[1].userId);

    }

    // TEST INSERT==========================================================
    @Test
    public void test_insert_1() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        User user1 = new User();
        user1.userId = 10;
        user1.userName = "Kepler";

        User user2 = new User();
        user2.userId = 20;
        user2.userName = "Ganymede";

        boolean success = crud.execInsert(
            new User[] { user1, user2 });

        User[] users = (User[]) crud.execSelectTable(User.class);

        assertTrue(success);
        assertEquals(7, users.length);// check count
        assertNotEquals(user1.userId, users[5].userId);
        assertNotEquals(user2.userId, users[6].userId);
        assertEquals((Integer) 6, users[5].userId);
        assertEquals((Integer) 7, users[6].userId);
        assertEquals(user1.userName, users[5].userName);
        assertEquals(user2.userName, users[6].userName);

    }

    @Test
    public void test_insert_2() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        User user1 = new User();
        user1.userName = "Kepler";

        User user2 = new User();
        user2.userName = "Kepler";

        boolean success = crud.execInsert(
            new User[] { user1, user2 });

        // check unique constraint (duplicate user_name)
        assertFalse(success);// expected FALSE

    }

    @Test
    public void test_insert_3_with_includeExclude() throws Exception {
        // initialize DB
        createDB();
        final D6Crud crud = createCrud();
        User user1 = new User();
        user1.userName = "Uranus";
        user1.marriedFlag = true;

        User user2 = new User();
        user2.userName = "Neptune";
        user2.marriedFlag = true;

        // married_flag is excluded
        boolean success = crud.execInsert(new User[] { user1, user2 },
            new D6Inex().addExcludeColumn("married_flag"));

        User[] users = (User[]) crud.execSelectTable(User.class);

        // check unique constraint (duplicate user_name)
        assertTrue(success);
        assertEquals((Integer) 6, users[5].userId);
        assertEquals((Integer) 7, users[6].userId);
        assertEquals(user1.userName, users[5].userName);
        assertEquals(user2.userName, users[6].userName);

        // married_flag should NOT be changed.Stay NULL on DB.
        assertNotEquals(user1.marriedFlag, users[5].marriedFlag);
        assertNotEquals(user2.marriedFlag, users[6].marriedFlag);
    }

    @Test
    public void test_insert_4_with_includeExclude() throws Exception {
        // initialize DB
        createDB();
        final D6Crud crud = createCrud();
        User user1 = new User();
        user1.userName = "Uranus";
        user1.marriedFlag = true;

        User user2 = new User();
        user2.userName = "Neptune";
        user2.marriedFlag = true;

        // married_flag is excluded
        boolean success = crud.execInsert(new User[] { user1, user2 }, new D6Inex().addIncludeColumn("user_name"));

        User[] users = (User[]) crud.execSelectTable(User.class);

        // check unique constraint (duplicate user_name)
        assertTrue(success);
        assertEquals((Integer) 6, users[5].userId);
        assertEquals((Integer) 7, users[6].userId);
        assertEquals(user1.userName, users[5].userName);
        assertEquals(user2.userName, users[6].userName);

        // married_flag should NOT be changed.Stay NULL on DB.
        assertNotEquals(user1.marriedFlag, users[5].marriedFlag);
        assertNotEquals(user2.marriedFlag, users[6].marriedFlag);
    }

    // TEST SELECT==========================================================
    @Test
    public void test_execSelectTable_0_by_sql() throws Exception {
        // initialize DB
        createDB();
        final D6Crud crud = createCrud();

        User[] users = (User[]) crud.execSelectTable("SELECT * FROM user", User.class);

        User user0 = users[0];

        assertEquals(5, users.length);
        assertEquals((Integer) 1, user0.userId);
        assertEquals("blackman", user0.userName);

        // long values 2000-06-19
        assertEquals(961340400000L, user0.birthDate.getTime());

        // long value means 13:00:10
        assertEquals(10800000L, user0.birthTime.getTime());
        assertEquals(true, user0.marriedFlag);
        assertEquals((Integer) 0, user0.schoolId);

        // long value means 2015-06-16 15:00:00.0
        assertEquals(1434434400000L, user0.createdAt.getTime());

    }

    @Test
    public void test_execSelectTable_1_by_model_clazz() throws Exception {
        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        User[] users = (User[]) crud.execSelectTable(User.class);

        User user0 = users[0];

        assertEquals(5, users.length);
        assertEquals((Integer) 1, user0.userId);
        assertEquals("blackman", user0.userName);

        // long values 2000-06-19
        assertEquals(961340400000L, user0.birthDate.getTime());

        // long values 13:00:10
        assertEquals(10800000L, user0.birthTime.getTime());
        assertEquals(true, user0.marriedFlag);
        assertEquals((Integer) 0, user0.schoolId);

        // long values 2015-06-16 15:00:00.0
        assertEquals(1434434400000L, user0.createdAt.getTime());

    }

    @Test
    public void test_execSelectTable_2_added_where_condition() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final String psql = "SELECT * FROM user WHERE married_flag=0";

        User[] users = (User[]) crud.execSelectTable(psql, User.class);

        assertEquals(2, users.length);

    }

    @Test
    public void test_execSelectTable_2_added_where_condition_with_wildcard() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final String psql = "SELECT * FROM user WHERE married_flag=?";

        final String[] searchKeys = new String[] { "0" };
        final User[] users = (User[]) crud.execSelectTable(psql, searchKeys, User.class);

        assertEquals(2, users.length);

    }

    @Test
    public void test_execSelectTable_3_added_WhereConditionObj_01() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final WhereCondition whereCondition = new WhereCondition().Col("married_flag").Equals().Val("0");
        final User[] users = (User[]) crud.execSelectTable(User.class, whereCondition);

        assertEquals(2, users.length);

    }

    @Test
    public void test_execSelectTable_4_added_WhereConditionObj_02() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final WhereCondition whereCondition = new WhereCondition().Col("married_flag").Equals().ValWildCard();
        final User[] users = (User[]) crud.execSelectTable(User.class, whereCondition, new Object[] { "0" });

        assertEquals(2, users.length);

    }

    @Test
    public void test_execSelectTableWithJoin_0() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final String psql = "SELECT * FROM user INNER JOIN school on user.school_id=school.school_id;";

        final Map<Class<?>, List<Object>> result = crud.execSelectTableWithJoin(psql, User.class, School.class);

        final User[] users = (User[]) crud.getAsModel(result, User.class);
        final School[] schools = (School[]) crud.getAsModel(result, School.class);

        assertEquals((Integer) 4, users[3].userId);
        assertEquals((Integer) 2, users[3].schoolId);
        assertEquals((Integer) 2, schools[3].schoolId);
        assertEquals("RatUniv", schools[3].schoolName);

    }

    @Test
    public void test_execSelectTableWithJoin_1_using_wild_card() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();
        final String psql = "SELECT * FROM user INNER JOIN school on user.school_id=school.school_id WHERE married_flag=?";

        final String[] searchKeys = new String[] { "0" };
        final Map<Class<?>, List<Object>> result = crud.execSelectTableWithJoin(psql, searchKeys, User.class, School.class);

        final User[] users = (User[]) crud.getAsModel(result, User.class);
        final School[] schools = (School[]) crud.getAsModel(result, School.class);

        assertEquals(2, users.length);
        assertEquals(2, schools.length);

        assertEquals((Integer) 2, users[0].userId);
        assertEquals((Integer) 0, users[0].schoolId);
        assertEquals((Integer) 0, schools[0].schoolId);

    }

    @Test
    public void test_execSelectCount_0_by_model_clazz() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        final int numberOfUsers = crud.execSelectCount(User.class);
        assertEquals(5, numberOfUsers);
    }

    @Test
    public void test_execSelectCount_1_by_sql() throws Exception {

        // initialize DB
        createDB();

        final D6Crud crud = createCrud();

        final int numberOfUsers = crud.execSelectCount("SELECT COUNT(*) FROM user");
        assertEquals(5, numberOfUsers);
    }

}