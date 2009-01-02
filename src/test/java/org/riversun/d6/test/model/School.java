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
@DBTable(tableName = "school")
public class School implements D6Model {
    @DBColumn(columnName = "school_id", columnType = "tinyint(4)", isNullable = false, isPrimaryKey = true)
    public Integer schoolId;

    @DBColumn(columnName = "school_name", columnType = "varchar(255)", isNullable = false, isPrimaryKey = true)
    public String schoolName;

    @DBColumn(columnName = "school_desc", columnType = "text")
    public String schoolDesc;

    @Override
    public String toString() {
        return "School [schoolId=" + schoolId + ", schoolName=" + schoolName + ", schoolDesc=" + schoolDesc + "]";
    }

}
