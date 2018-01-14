package org.riversun.d6.test.prepare_test;

import org.riversun.d6.DBConnInfo;

/**
 * Definition of RDBMS
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestHelper_TestDBDef {

    // EDIT HERE FOR YOUR ENVIRONMENT
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//"com.mysql.jdbc.Driver";

    // EDIT HERE FOR YOUR ENVIRONMENT
    public static final String DATABASE_URL = "jdbc:mysql://localhost/";

    // EDIT HERE FOR YOUR ENVIRONMENT
    public static final String USER = "[USERNAME_OF_THE_DB]";

    // EDIT HERE FOR YOUR ENVIRONMENT
    public static final String PASS = "[PASSWORD_OF_THE_DB]";

    public static final String TEST_DATABASE_NAME = "d6_test_db";

    public static DBConnInfo createTestDBConnInfo() {
        DBConnInfo dbConnInfo = new DBConnInfo();

        dbConnInfo.DBDriver = JDBC_DRIVER;
        dbConnInfo.DBUrl = DATABASE_URL + TEST_DATABASE_NAME;
        dbConnInfo.DBUser = USER;
        dbConnInfo.DBPassword = PASS;

        return dbConnInfo;
    }
}
