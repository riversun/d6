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
import java.util.Set;

import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBTable;
import org.riversun.string_grabber.StringGrabber;

/**
 * Helper class to INSEDRT statement<br>
 * -Populate properties of the model object into prepared statement<br>
 * <br>
 * Called from D6Crud
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6CrudInsertHelper extends D6CrudHelperBase {

    public D6CrudInsertHelper(Class<? extends D6Model> modelClazz) {
        super(modelClazz);
    }

    /**
     * Map model object properties to DB(prepared statement)
     * 
     * @param mModelObj
     * @param preparedStatement
     * @throws D6Exception
     */
    void map(D6Model mModelObj, PreparedStatement preparedStatement, D6Inex includeExcludeColumnNames) throws D6Exception {

        log("#map obj=" + mModelObj);

        final Set<String> columnNameSet = getAllColumnNames();

        // index starts from 1
        int parameterIndex = 1;

        if (includeExcludeColumnNames != null) {
            includeExcludeColumnNames.manipulate(columnNameSet);
        }

        for (String columnName : columnNameSet) {

            final D6ModelClassFieldInfo fieldInfo = getFieldInfo(columnName);

            final boolean isAutoIncrement = fieldInfo.isAutoIncrement;
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
            if (fieldValue == null && isNullable == false && isAutoIncrement == false) {
                // - In case found the null-value column and increment flag is
                // false even though this item is NOT NULL
                throw new D6Exception("D6Error the model object field '" + fieldName + "'(column name is " + columnName + ") should not null." + "Because of constraint of table definition.");
            }

            //
            if (isAutoIncrement == false) {
                try {

                    log("#map idx=" + parameterIndex + " " + columnName + "=" + fieldValue + " (" + type + ")");

                    setValue(parameterIndex, preparedStatement, type, fieldValue);

                } catch (Exception e) {
                    throw new D6Exception(e);
                }

                parameterIndex++;
            }

        }// end for (String columnName : columnNameSet) {

    }

    /**
     * Generate INSERT preparedSQL statement
     * 
     * @return
     */
    String createInsertPreparedSQLStatement() {
        final D6Inex includeExcludeColumnNames = null;
        boolean ignoreDuplicate=false;
        return createInsertPreparedSQLStatement(includeExcludeColumnNames,ignoreDuplicate);

    }

    /**
     * Generate INSERT preparedSQL statement
     * 
     * @param includeExcludeColumnNames
     * @return
     */
    String createInsertPreparedSQLStatement(D6Inex includeExcludeColumnNames,boolean ignoreDuplicate) {

        final Set<String> columnNameSet = getAllColumnNames();

        final StringGrabber sgSQL = new StringGrabber();

        // Get the table name
        final DBTable table = mModelClazz.getAnnotation(DBTable.class);
        final String tableName = table.tableName();

        if(ignoreDuplicate){
        	sgSQL.append("INSERT IGNORE INTO " + tableName + " (");
        }else{
        sgSQL.append("INSERT INTO " + tableName + " (");
        }
        final StringGrabber sgColumnNames = new StringGrabber();
        final StringGrabber sgValues = new StringGrabber();

        if (includeExcludeColumnNames != null) {
            includeExcludeColumnNames.manipulate(columnNameSet);
        }

        for (String columnName : columnNameSet) {

            final D6ModelClassFieldInfo fieldInfo = getFieldInfo(columnName);

            //
            final boolean isAutoIncrement = fieldInfo.isAutoIncrement;

            if (isAutoIncrement == false) {
                // - if auto increment is off
                sgColumnNames.append(columnName);
                sgColumnNames.append(", ");
                sgValues.append("?");
                sgValues.append(", ");
            } else {
                // - if auto increment is available
                // it's not the target of the update
            }

        }// end for

        if (sgColumnNames.length() > 2) {
            sgColumnNames.removeTail(2);
        }
        if (sgValues.length() > 2) {
            sgValues.removeTail(2);
        }

        sgSQL.append(sgColumnNames.toString());
        sgSQL.append(") ");
        sgSQL.append("VALUES ( ");
        sgSQL.append(sgValues.toString());
        sgSQL.append(" )");

        final String sql = sgSQL.toString();

        log("#createInsertPreparedSQLStatement sql=" + sql);

        return sql;
    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {
        D6Logger.loge(this.getClass(), msg, e);
    }
}
