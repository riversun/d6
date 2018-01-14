/*  d6 Lightweight O/R mapper for java with ease of use 
 *
 *  Copyright (c) 2006- Tom Misawa, riversun.org@gmail.com
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 *  
 */
package org.riversun.d6.predev;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.riversun.string_grabber.StringGrabber;
import org.riversun.string_grabber.StringGrabberList;

/**
 * Utility class to generate the Java Model Class code from the MySQL table
 * definitions(String obtained by the "desc table name")
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6JavaModelGen4MySQL {

  /**
   * Java And MYSQL mapping
   * {@see http://dev.mysql.com/doc/connector-j/en/connector
   * -j-reference-type-conversions.html}
   */
  private static final String JAVA_TYPE_INTEGER = "Integer";
  private static final String JAVA_TYPE_LONG = "Long";
  private static final String JAVA_TYPE_BOOLEAN = "boolean";

  private static final String JAVA_COMPLEX_TYPE_GEOMETRY = "org.riversun.d6.model.Geometry";

  private static final String JAVA_TYPE_TIMESTAMP = "java.sql.Timestamp";
  private static final String JAVA_TYPE_DATE1 = "java.sql.Date";
  private static final String JAVA_TYPE_TIME = "java.sql.Time";

  private static final String JAVA_TYPE_STRING = "String";
  private static final String DQ = "\"";

  private static final boolean IS_DISPLAY_THOSE_OPTIONAL_ON_DB_COLUMN = false;

  public static class DBDefinition {
    public String dbTableName;
    public String javaClassName;
    public List<DBColumnDef> columnList;
  }

  public static class DBColumnDef {
    public String dbColumnName;
    public String dbColumnType;
    public String javaField;
    public String javaType;
    public boolean isNullable = true;
    public boolean isPrimaryKey = false;
    public boolean isAutoIncrement = false;
    public boolean isUnique = false;

    @Override
    public String toString() {
      return "DBColumnDef [dbColumnName=" + dbColumnName + ", dbColumnType=" + dbColumnType + ", javaField=" + javaField + ", javaType=" + javaType
          + ", isNullable=" + isNullable
          + ", isPrimaryKey=" + isPrimaryKey + ", isAutoIncrement=" + isAutoIncrement + ", isUnique=" + isUnique + "]";
    }

  }

  public String generateModelClass(String mysqlTableDesc) {
    return generateModelClass(getDatabaseDefinitionFromMySqlTableDescText(mysqlTableDesc));
  }

  /**
   * [EN]generate model class text<br>
   * 
   * @param dbDefinition
   * @return
   */
  public String generateModelClass(DBDefinition dbDefinition) {
    StringGrabber sgOutputModelCalssText = new StringGrabber();

    sgOutputModelCalssText.append("@DBTable(tableName=" + DQ + dbDefinition.dbTableName + DQ + ")");

    sgOutputModelCalssText.newLine();
    sgOutputModelCalssText.append("public class " + dbDefinition.javaClassName + " implements D6Model");
    sgOutputModelCalssText.newLine();
    sgOutputModelCalssText.append("{");
    sgOutputModelCalssText.newLine();

    final int columnCount = dbDefinition.columnList.size();
    for (int i = 0; i < columnCount; i++) {

      DBColumnDef column = dbDefinition.columnList.get(i);

      StringGrabber sg = new StringGrabber();
      sg.append("columnName=" + DQ + column.dbColumnName + DQ);
      sg.append(", ");
      sg.append("columnType=" + DQ + column.dbColumnType + DQ);
      sg.append(", ");
      if (column.isNullable) {
        if (IS_DISPLAY_THOSE_OPTIONAL_ON_DB_COLUMN) {
          // NOT NEED TO SHOW
          sg.append("isNullable=");
          sg.append("true");
          sg.append(", ");
        }
      } else {
        sg.append("isNullable=");
        sg.append("false");
        sg.append(", ");

      }
      if (column.isPrimaryKey) {
        sg.append("isPrimaryKey=");
        sg.append("true");
        sg.append(", ");
      } else {
        if (IS_DISPLAY_THOSE_OPTIONAL_ON_DB_COLUMN) {
          // NOT NEED TO SHOW
          sg.append("isPrimaryKey=");
          sg.append("true");
          sg.append(", ");
        }
      }

      if (column.isUnique) {
        sg.append("isUnique=");
        sg.append("true");
        sg.append(", ");
      } else {
        if (IS_DISPLAY_THOSE_OPTIONAL_ON_DB_COLUMN) {
          // NOT NEED TO SHOW
          sg.append("isUnique=");
          sg.append("true");
          sg.append(", ");
        }
      }
      if (column.isAutoIncrement) {
        sg.append("isAutoIncrement=");
        sg.append("true");
        sg.append(", ");
      } else {
        if (IS_DISPLAY_THOSE_OPTIONAL_ON_DB_COLUMN) {
          sg.append("isAutoIncrement=");
          sg.append("false");
          sg.append(", ");
        }
      }
      sg.removeTail(2);

      String codeLine0 = "@DBColumn(" + sg.toString() + ")";
      String codeLine1 = "public " + column.javaType + " " + column.javaField + ";";

      sgOutputModelCalssText.append(codeLine0);
      sgOutputModelCalssText.newLine();
      sgOutputModelCalssText.append(codeLine1);
      sgOutputModelCalssText.newLine();
      sgOutputModelCalssText.newLine();

    }

    sgOutputModelCalssText.newLine();
    sgOutputModelCalssText.append("}");

    return sgOutputModelCalssText.toString();
  }

  /**
   * 
   * Get the database definition class from MySQL database desc string
   * 
   * @param mySqlTableDescText
   * @return
   */
  public DBDefinition getDatabaseDefinitionFromMySqlTableDescText(String mySqlTableDescText) {

    final int descLineNumber = 0;
    final int fieldLineStartNumber = 4;

    final DBDefinition dbDefinition = new DBDefinition();

    final StringGrabber sgMysqlTableDescText = new StringGrabber();
    sgMysqlTableDescText.append(mySqlTableDescText);

    final StringGrabberList sgList = sgMysqlTableDescText.toSgList();

    dbDefinition.dbTableName = new StringGrabber(sgList.get(descLineNumber).toString()).getStringEnclosedIn("desc ", ";").get(0).toString();
    dbDefinition.javaClassName = new StringGrabber(getCamelCaseFromUnderscoreSeparated(dbDefinition.dbTableName)).replaceFirstToUpperCase().toString();
    dbDefinition.columnList = new ArrayList<DBColumnDef>();

    for (int i = fieldLineStartNumber; i < sgList.size() - 1; i++) {

      final StringGrabber dbDefLine = sgList.get(i);
      final StringGrabberList defList = dbDefLine.getStringEnclosedIn("|", "|");

      final DBColumnDef columnDef = new DBColumnDef();

      //
      final StringGrabber rawFieldName = defList.get(0).replace(" ", "");
      final StringGrabber rawType = defList.get(1).removeHead(1);
      final StringGrabber rawNull = defList.get(2).replace(" ", "");
      final StringGrabber rawKey = defList.get(3).replace(" ", "");
      final StringGrabber rawDefault = defList.get(4).replace(" ", "");
      final StringGrabber rawExtra = defList.get(5).replace(" ", "");

      columnDef.dbColumnName = removeTailSpace(rawFieldName.toString());
      columnDef.dbColumnType = removeTailSpace(rawType.toString());
      columnDef.isNullable = "YES".equals(removeTailSpace(rawNull.toString()));
      columnDef.isPrimaryKey = "PRI".equals(removeTailSpace(rawKey.toString()));
      columnDef.isUnique = "UNI".equals(removeTailSpace(rawKey.toString()));
      columnDef.isAutoIncrement = "auto_increment".equals(removeTailSpace(rawExtra.toString()));
      //
      columnDef.javaField = getCamelCaseFromUnderscoreSeparated(columnDef.dbColumnName);
      columnDef.javaType = getJavaTypeFromMySQLType(columnDef.dbColumnType);

      dbDefinition.columnList.add(columnDef);
    }

    return dbDefinition;
  }

  /**
   * 
   * returns string that have been removed at the end of the sequence of space
   * string
   * 
   * @param str
   * @return
   */
  private String removeTailSpace(String str) {

    final int lastIndexOfStr = str.length() - 1;

    if (str.endsWith(" ")) {

      for (int i = lastIndexOfStr; i >= 0; i--) {

        if (str.charAt(i) == ' ') {

        } else {

          final int lastIndexToSubstring = i + 1;

          str = str.substring(0, lastIndexToSubstring);
          break;
        }
      }

    }
    return str;
  }

  private Map<String, String> mTypeMap;

  /**
   * 
   * Returns Java type name corresponding to MySQL column data type
   * 
   * @param mySqlType
   * @return
   */
  private String getJavaTypeFromMySQLType(String mySqlType) {
    if (mTypeMap == null) {
      mTypeMap = new LinkedHashMap<String, String>();

      // Types To String
      mTypeMap.put("char", JAVA_TYPE_STRING);
      mTypeMap.put("varchar", JAVA_TYPE_STRING);
      mTypeMap.put("text", JAVA_TYPE_STRING);
      mTypeMap.put("longtext", JAVA_TYPE_STRING);

      // Types To Date
      mTypeMap.put("datetime", JAVA_TYPE_TIMESTAMP);
      mTypeMap.put("date", JAVA_TYPE_DATE1);
      mTypeMap.put("timestamp", JAVA_TYPE_TIMESTAMP);
      mTypeMap.put("time", JAVA_TYPE_TIME);

      // Types To boolean
      mTypeMap.put("tinyint(1)", JAVA_TYPE_BOOLEAN);

      mTypeMap.put("int,unsigned", JAVA_TYPE_LONG);

      // Types To Integer
      mTypeMap.put("int", JAVA_TYPE_INTEGER);
      mTypeMap.put("mediumint", JAVA_TYPE_INTEGER);
      mTypeMap.put("tinyint", JAVA_TYPE_INTEGER);

      // Types To Geometry
      mTypeMap.put("geometry", JAVA_COMPLEX_TYPE_GEOMETRY);

    }

    for (String typeStr : mTypeMap.keySet()) {

      if (typeStr.contains(",")) {
        if (mySqlType.startsWith(typeStr.split(",")[0]) && mySqlType.contains(typeStr.split(",")[1])) {
          return mTypeMap.get(typeStr);
        }
      } else {
        if (mySqlType.startsWith(typeStr)) {
          return mTypeMap.get(typeStr);
        }
      }
    }

    return null;

  }

  /**
   * Convert underscore separated text into camel case text
   * 
   * @param underScoreSeparatedStr
   * @return
   */
  private String getCamelCaseFromUnderscoreSeparated(String underScoreSeparatedStr) {

    final StringGrabber underScoreSeparatedSg = new StringGrabber(underScoreSeparatedStr);
    final StringGrabberList wordBlockList = underScoreSeparatedSg.split("_");
    final StringGrabber resultSg = new StringGrabber();

    for (int i = 0; i < wordBlockList.size(); i++) {

      final StringGrabber word = wordBlockList.get(i);

      if (i == 0) {

        // first element not to upper case
        resultSg.append(word.toString());

      } else {
        resultSg.append(word.replaceFirstToUpperCase().toString());
      }
    }
    return resultSg.toString();
  }

}
