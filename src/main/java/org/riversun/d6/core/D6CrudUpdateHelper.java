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
package org.riversun.d6.core;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.annotation.DBTable;
import org.riversun.string_grabber.StringGrabber;

/**
 * Helper class to perform the following using the model classes<br>
 * -To generate UPDATE statement<br>
 * -Populate properties of the model object into prepared statement<br>
 * <br>
 * Called from D6Crud
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6CrudUpdateHelper extends D6CrudHelperBase {

    public D6CrudUpdateHelper(Class<? extends D6Model> modelClazz) {
        super(modelClazz);

    }

    /**
     * Generate INSERT preparedSQL statement
     * 
     * @param policy
     *            RAW_SQL or PREPARED_STATEMENT
     * @return
     */
    String createUpdatePreparedSQLStatement(D6Inex includeExcludeColumnNames) {

        final Set<String> columnNameSet = getAllColumnNames();

        final StringGrabber sgSQL = new StringGrabber();

        // Get the table name
        final DBTable table = mModelClazz.getAnnotation(DBTable.class);
        final String tableName = table.tableName();

        sgSQL.append("UPDATE " + tableName + " SET ");

        if (includeExcludeColumnNames != null) {
            // edit column names corresponding to include/exclude policy
            includeExcludeColumnNames.manipulate(columnNameSet);
        }
        // Scan all column names in the model class
        for (String columnName : columnNameSet) {

            sgSQL.append(columnName);
            sgSQL.append(" = ?, ");

        }// end of for (String columnName : columnNameSet) {

        if (sgSQL.length() > 2) {
            sgSQL.removeTail(2);
        }

        // add SQL where clause
        sgSQL.append(" ");
        sgSQL.append(getWhereClause());

        final String sql = sgSQL.toString();
        log("#createUpdatePreparedSQLStatement sql=" + sql);

        return sql;
    }

    /**
     * generate where clause of current model class
     * 
     * @return
     */
    String getWhereClause() {

        final StringGrabber sgWhere = new StringGrabber();

        List<Field> primaryKeyFieldList = getPrimaryKeyFieldList();

        if (primaryKeyFieldList.size() > 0) {

            sgWhere.append("WHERE ");

            for (Field field : primaryKeyFieldList) {
                DBColumn dbColumn = field.getAnnotation(DBColumn.class);

                sgWhere.append(dbColumn.columnName());
                sgWhere.append(" = ? AND ");

            }

            sgWhere.removeTail(5);
        }

        return sgWhere.toString();
    }

    /**
     * Map model object properties to DB(prepared statement)
     * 
     * @param mModelObj
     * @param preparedStatement
     * @throws D6Exception
     */
    final void map(D6Model mModelObj, PreparedStatement preparedStatement, D6Inex includeExcludeColumnNames) throws D6Exception {

        log("#map obj=" + mModelObj);

        final Set<String> columnNameSet = getAllColumnNames();

        // index starts from 1
        int parameterIndex = 1;

        final List<String> columnNameList = new ArrayList<String>(columnNameSet);

        final List<Field> primaryKeyFieldList = getPrimaryKeyFieldList();

        for (Field field : primaryKeyFieldList) {
            DBColumn dbColumn = field.getAnnotation(DBColumn.class);
            String columnName = dbColumn.columnName();
            columnNameList.add(columnName);
        }

        // edit column names corresponding to include/exclude policy
        if (includeExcludeColumnNames != null) {
            includeExcludeColumnNames.manipulate(columnNameList);
        }

        // scan column by column
        for (String columnName : columnNameList) {

            final D6ModelClassFieldInfo fieldInfo = getFieldInfo(columnName);

            final boolean isNullable = fieldInfo.isNullable;

            final Field field = fieldInfo.field;
            final String fieldName = field.getName();

            final Class<?> type = field.getType();

            Object fieldValue = null;

            try {
                fieldValue = field.get(mModelObj);
            } catch (IllegalArgumentException e) {
                throw new D6Exception(e);
            } catch (IllegalAccessException e) {
                throw new D6Exception(e);
            }

            //
            if (fieldValue == null && isNullable == false) {
                // In case found the null-value column,
                // even though this item is NOT NULL
                throw new D6Exception("D6Error the model object field '" + fieldName + "'(column name is " + columnName + ") should not null." + "Because of constraint of table definition.");
            }

            try {

                log("#map idx=" + parameterIndex + " " + columnName + "=" + fieldValue + " (" + type + ")");

                setValue(parameterIndex, preparedStatement, type, fieldValue);

            } catch (Exception e) {
                throw new D6Exception(e);
            }

            parameterIndex++;

        }// end for (String columnName : columnNameList) {

    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {
        D6Logger.loge(this.getClass(), msg, e);
    }
}
