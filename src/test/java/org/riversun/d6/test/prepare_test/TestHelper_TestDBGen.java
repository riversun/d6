package org.riversun.d6.test.prepare_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;

/**
 * 
 * Create Database for testing use.
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestHelper_TestDBGen {
  public static void main(String[] args) {
    new TestHelper_TestDBGen().createTestDB();
  }

  public void createTestDB() {

    Connection conn = null;
    Statement stmt = null;

    final String dbName = TestHelper_TestDBDef.TEST_DATABASE_NAME;

    try {

      conn = DriverManager.getConnection(TestHelper_TestDBDef.DATABASE_URL
          + "?characterEncoding=UTF-8&serverTimezone=JST",
          TestHelper_TestDBDef.USER,
          TestHelper_TestDBDef.PASS);

      stmt = conn.createStatement();

      {
        // CREATE DATABASE
        final String sql = "DROP DATABASE IF EXISTS " + dbName;
        stmt.executeUpdate(sql);
      }

      {

        // CREATE TABLES
        stmt.executeUpdate("CREATE DATABASE " + dbName);

        // user table
        stmt.executeUpdate("DROP TABLE IF EXISTS " + dbName + "." + "user");
        stmt.executeUpdate("CREATE TABLE " + dbName + "." + "user" + " (user_id tinyint(4) NOT NULL AUTO_INCREMENT, user_name varchar(255) UNIQUE NOT NULL, "
            + "birth_date date, birth_time time,married_flag tinyint(1),school_id tinyint(4), " + "created_at datetime, PRIMARY KEY (user_id));");
        // school table
        stmt.executeUpdate("DROP TABLE IF EXISTS " + dbName + "." + "school");
        stmt
            .executeUpdate("CREATE TABLE "
                + dbName
                + "."
                + "school"
                + " (school_id tinyint(4) NOT NULL, school_name varchar(255) NOT NULL, school_desc text,school_nickname varchar(255), PRIMARY KEY (school_id,school_name), INDEX (school_nickname))");

      }

      {

        // INSERT TABLES
        // null->AUTO_INCREMENTS
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "user VALUES (null,'blackman','2000-06-19','12:00:00',1,0,'2015-06-16 15:00:00')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "user VALUES (null,'blueman','2001-02-01','13:00:10',0,0,'2015-06-17 15:00:00')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "user VALUES (null,'redman','2002-03-01','14:35:10',1,1,'2015-06-18 15:12:00')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "user VALUES (null,'purpleman','2003-03-01','14:35:10',1,2,'2015-06-19 15:50:00')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "user VALUES (null,'greenman','2004-05-01','15:10:29',0,1,'2015-06-20 15:20:00')");

        // school
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "school VALUES (0,'BirdUniv','The best MBA school.','BU')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "school VALUES (1,'CatUniv','Educational development at California.','CU')");
        stmt.executeUpdate("INSERT INTO " + dbName + "." + "school VALUES (2,'RatUniv','Mathmatical education.','MU')");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {

      try {
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }

  }
}
