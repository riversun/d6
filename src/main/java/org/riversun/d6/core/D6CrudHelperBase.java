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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;

/**
 * 
 * Base class with common method for CRUD helper
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public abstract class D6CrudHelperBase {

    final Class<?> mModelClazz;
    final Map<String, D6ModelClassFieldInfo> mColumnNameFieldInfoMap;

    public D6CrudHelperBase(Class<? extends D6Model> modelClazz) {
        this.mModelClazz = modelClazz;
        this.mColumnNameFieldInfoMap = new ModelClazzColumnNameAndFieldMapper(mModelClazz).build();

    }

    /**
     * Returns Field info by columnName
     * 
     * @param columnName
     * @return
     */
    final D6ModelClassFieldInfo getFieldInfo(String columnName) {
        final D6ModelClassFieldInfo fieldInfo = mColumnNameFieldInfoMap.get(columnName);
        return fieldInfo;
    }

    /**
     * Returns Set of columName of model class
     * 
     * @return
     */
    final Set<String> getAllColumnNames() {
        final Set<String> columnNameSet = mColumnNameFieldInfoMap.keySet();

        // Returns clone of columnNameSet because {@see D6Inex} directly
        // manipulats(almost delete) columnNameSet,so it effects
        // mColumnNameFieldInfoMap.
        // So, prevent original columnNameSet from getting edit.
        return new LinkedHashSet<String>(columnNameSet);
    }

    /**
     * Returns Set of primary key column name of model class
     * 
     * @return
     */
    final Set<String> getPrimaryColumnNames() {
        final List<DBColumn> primaryKeyColumnList = getPrimaryKeyColumnList();
        final Set<String> primaryKeyColumnNameSet = new LinkedHashSet<String>();
        for (DBColumn col : primaryKeyColumnList) {

            primaryKeyColumnNameSet.add(col.columnName());
        }
        return primaryKeyColumnNameSet;
    }

    /**
     * Set value to preparedstatement with appropriate cast
     * 
     * @param parameterIndex
     * @param preparedStatement
     * @param fieldType
     * @param fieldValue
     * @throws Exception
     */
    void setValue(int parameterIndex, PreparedStatement preparedStatement, Class<?> fieldType, Object fieldValue) throws Exception {

        try {

            if (fieldType == String.class) {
                preparedStatement.setString(parameterIndex, (String) fieldValue);
            } else if (fieldType == java.sql.Timestamp.class) {
                preparedStatement.setTimestamp(parameterIndex, (java.sql.Timestamp) fieldValue);
            } else if (fieldType == java.sql.Date.class) {
                preparedStatement.setDate(parameterIndex, (java.sql.Date) fieldValue);
            } else if (fieldType == java.sql.Time.class) {
                preparedStatement.setTime(parameterIndex, (java.sql.Time) fieldValue);
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {

                if (fieldValue != null) {
                    boolean boolValue = (boolean) (Boolean) fieldValue;
                    preparedStatement.setInt(parameterIndex, (boolValue ? 1 : 0));
                } else {
                    preparedStatement.setInt(parameterIndex, 0);

                }
            } else if (fieldType == int.class || fieldType == Integer.class) {

                if (fieldValue != null) {
                    preparedStatement.setInt(parameterIndex, (int) (Integer) fieldValue);
                } else {
                    preparedStatement.setNull(parameterIndex, java.sql.Types.INTEGER);
                }

            } else {
                final String msg = "Unknown data type. type=" + fieldType + " value=" + fieldValue;
                loge(msg);
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            loge("#setValue", e);
            throw e;
        }

    }

    /**
     * get primary key field of model class
     * 
     * @return
     */
    final List<Field> getPrimaryKeyFieldList() {

        final List<Field> fieldList = new ArrayList<Field>();

        final Set<String> columnNameSet = getAllColumnNames();

        for (String columnName : columnNameSet) {

            final D6ModelClassFieldInfo fieldInfo = getFieldInfo(columnName);

            final Field field = fieldInfo.field;

            final DBColumn dbColumn = field.getAnnotation(DBColumn.class);

            if (dbColumn.isPrimaryKey()) {

                fieldList.add(field);
            }
        }

        return fieldList;
    }

    /**
     * Returns DBColumn list of primary key
     * 
     * @return
     */
    final List<DBColumn> getPrimaryKeyColumnList() {

        final List<DBColumn> primaryKeyColumnList = new ArrayList<DBColumn>();

        final Set<String> columnNameSet = getAllColumnNames();

        for (String columnName : columnNameSet) {

            final D6ModelClassFieldInfo fieldInfo = getFieldInfo(columnName);

            final Field field = fieldInfo.field;

            final DBColumn dbColumn = field.getAnnotation(DBColumn.class);

            if (dbColumn.isPrimaryKey()) {

                primaryKeyColumnList.add(dbColumn);
            }
        }

        return primaryKeyColumnList;
    }

    /**
     * log method for standard log
     * 
     * @param msg
     *            standard log message
     */
    abstract void log(String msg);

    /**
     * log method for error log
     * 
     * @param msg
     *            error log message
     * @param e
     *            related exceptions
     */
    abstract void loge(String msg, Exception... e);
}
