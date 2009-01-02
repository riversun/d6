package org.riversun.d6.test.prepare_test;

import org.riversun.d6.predev.D6JavaModelGen4MySQL;

/**
 * 
 * Test helper to generate the Java Model Class code from the MySQL table
 * definitions in the console. :) <br>
 * <br>
 * Use 'DESC [table]' command and paste it.
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestHelper_ModelClazzSourceCodeGen_FromTableDesc {

    public static void main(String[] args) {

        D6JavaModelGen4MySQL d6JavaModelGen4MySQL = new D6JavaModelGen4MySQL();

        // generate model code of model class.You can copy and past it into your
        // favorite IDE.:)
        System.out.println(d6JavaModelGen4MySQL.generateModelClass(DESC_SCHOOL));
        System.out.println(d6JavaModelGen4MySQL.generateModelClass(DESC_USER));

    }

    public static final String DESC_USER = //

            "mysql> desc user;" + "\n" + //
                    "+--------------+--------------+------+-----+---------+----------------+" + "\n" + //
                    "| Field        | Type         | Null | Key | Default | Extra          |" + "\n" + //
                    "+--------------+--------------+------+-----+---------+----------------+" + "\n" + //
                    "| user_id      | tinyint(4)   | NO   | PRI | NULL    | auto_increment |" + "\n" + //
                    "| user_name    | varchar(255) | NO   | UNI | NULL    |                |" + "\n" + //
                    "| birth_date   | date         | YES  |     | NULL    |                |" + "\n" + //
                    "| birth_time   | time         | YES  |     | NULL    |                |" + "\n" + //
                    "| married_flag | tinyint(1)   | YES  |     | NULL    |                |" + "\n" + //
                    "| school_id    | tinyint(4)   | YES  |     | NULL    |                |" + "\n" + //
                    "| created_at   | datetime     | YES  |     | NULL    |                |" + "\n" + //
                    "+--------------+--------------+------+-----+---------+----------------+" + "\n";

    public static final String DESC_SCHOOL = //

            "mysql> desc school;" + "\n" + //
                    "+-------------+--------------+------+-----+---------+-------+" + "\n" + //
                    "| Field       | Type         | Null | Key | Default | Extra |" + "\n" + //
                    "+-------------+--------------+------+-----+---------+-------+" + "\n" + //
                    "| school_id   | tinyint(4)   | NO   | PRI | NULL    |       |" + "\n" + //
                    "| school_name | varchar(255) | NO   | PRI | NULL    |       |" + "\n" + //
                    "| school_desc | text         | YES  |     | NULL    |       |" + "\n" + //
                    "+-------------+--------------+------+-----+---------+-------+" + "\n";

}
