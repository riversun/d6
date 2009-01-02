package org.riversun.d6.test.model;

import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.annotation.DBTable;

/**
 * Model class for Testing D6
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
@DBTable(tableName = "user")
public class User implements D6Model {
    @DBColumn(columnName = "user_id", columnType = "tinyint(4)", isNullable = false, isPrimaryKey = true, isAutoIncrement = true)
    public Integer userId;

    @DBColumn(columnName = "user_name", columnType = "varchar(255)", isNullable = false, isUnique = true)
    public String userName;

    @DBColumn(columnName = "birth_date", columnType = "date")
    public java.sql.Date birthDate;

    @DBColumn(columnName = "birth_time", columnType = "time")
    public java.sql.Time birthTime;

    @DBColumn(columnName = "married_flag", columnType = "tinyint(1)")
    public boolean marriedFlag;

    @DBColumn(columnName = "school_id", columnType = "tinyint(4)")
    public Integer schoolId;

    @DBColumn(columnName = "created_at", columnType = "datetime")
    public java.sql.Timestamp createdAt;

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", birthDate=" + birthDate + ", birthTime=" + birthTime + ", marriedFlag=" + marriedFlag + ", schoolId=" + schoolId
                + ", createdAt=" + createdAt + "]";
    }

}
