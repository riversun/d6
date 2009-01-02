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

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.riversun.d6.D6Model;
import org.riversun.d6.DBConnCreator;
import org.riversun.d6.DBConnInfo;
import org.riversun.d6.annotation.DBTable;

/**
 * Class with crud methods
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class D6Crud {

    DBConnInfo mConnInfo;

    /**
     * Constructor
     * 
     * @param connInfo
     */
    public D6Crud(DBConnInfo connInfo) {
        this.mConnInfo = connInfo;
    }

    // Methods_of_DELETE///////////////////////////////////////////////////

    /**
     * Delete the appropriate line in the specified model object
     * 
     * @param modelObj
     * @return true:DB operation success false:failure
     */
    public boolean execDelete(Class<? extends D6Model> modelObj) {

        boolean retVal = false;

        final D6CrudDeleteHelper dh = new D6CrudDeleteHelper(modelObj);
        final String updateSQL = dh.createDeleteAllPreparedSQLStatement();

        log("#execDelete model=" + modelObj + " deleteSQL=" + updateSQL);

        final Connection conn = createConnection();

        try {

            PreparedStatement preparedStmt = null;

            // There is a possibility that the error occurs in one single delete
            // statement.
            // Therefore, turn the auto commit off.
            conn.setAutoCommit(false);

            preparedStmt = conn.prepareStatement(updateSQL);

            // execute SQL
            preparedStmt.executeUpdate();

            // Finally, commit.
            conn.commit();

            retVal = true;

        } catch (SQLException e) {

            loge("#execDelete", e);
            retVal = false;

        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                retVal = false;
                loge("#execDelete", e);
            }
        }
        return retVal;

    }

    /**
     * Delete the appropriate lines in the specified model object
     * 
     * @param modelObj
     * @return true:DB operation success false:failure
     */
    public boolean execDelete(D6Model modelObj) {
        return execDelete(new D6Model[] { modelObj });
    }

    /**
     * Delete the appropriate lines in specified model objects
     * 
     * @param modelObjs
     * @return true:DB operation success false:failure
     */
    public boolean execDelete(D6Model[] modelObjs) {

        if (modelObjs == null || modelObjs.length == 0) {
            return false;
        }

        boolean retVal = false;

        final D6CrudDeleteHelper dh = new D6CrudDeleteHelper(modelObjs[0].getClass());
        final String deleteSQL = dh.createDeletePreparedSQLStatement();

        log("#execDelete modelObjs=" + modelObjs + " delete SQL=" + deleteSQL);

        final Connection conn = createConnection();

        try {

            PreparedStatement preparedStmt = null;

            // There is a possibility that the error occurs in one single delete
            // statement.
            // Therefore, turn the auto commit off.
            conn.setAutoCommit(false);

            preparedStmt = conn.prepareStatement(deleteSQL);

            for (D6Model modelObj : modelObjs)
            {
                dh.map(modelObj, preparedStmt);

                // execute SQL
                preparedStmt.executeUpdate();
            }

            // Finally, commit.
            conn.commit();

            retVal = true;

        } catch (SQLException e) {

            loge("#execDelete", e);
            retVal = false;

        } catch (D6Exception e) {

            // catch from helper
            loge("#execDelete", e);
            retVal = false;

        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                retVal = false;
                loge("#execDelete", e);
            }
        }
        return retVal;

    }

    /**
     * Update Model Object
     * 
     * @param modelObj
     * @return true:DB operation success false:failure
     */
    public boolean execUpdate(D6Model modelObj) {
        final D6Inex includeExcludeColumnNames = null;
        return execUpdate(modelObj, includeExcludeColumnNames);
    }

    /**
     * Update Model Object
     * 
     * @param modelObj
     * @param includeExcludeColumnNames
     * @return true:DB operation success false:failure
     */
    public boolean execUpdate(D6Model modelObj, D6Inex includeExcludeColumnNames) {

        boolean retVal = false;

        if (modelObj == null) {
            return retVal;
        }

        final D6CrudUpdateHelper d6CrudUpdateHelper = new D6CrudUpdateHelper(modelObj.getClass());

        final String updateSQL = d6CrudUpdateHelper.createUpdatePreparedSQLStatement(includeExcludeColumnNames);

        log("#execUpdate updateSQL=" + updateSQL);

        final Connection conn = createConnection();

        try {

            PreparedStatement preparedStmt = null;

            // There is a possibility that the error occurs in one single update
            // statement.
            // Therefore, turn the auto commit off.
            conn.setAutoCommit(false);

            preparedStmt = conn.prepareStatement(updateSQL);

            d6CrudUpdateHelper.map(modelObj, preparedStmt, includeExcludeColumnNames);

            // execute SQL
            preparedStmt.executeUpdate();

            // finally commit
            conn.commit();

            retVal = true;

        } catch (SQLException e) {

            loge("#execUpdate", e);
            retVal = false;

        } catch (D6Exception e) {

            // catch from helper
            loge("#execUpdate", e);
            retVal = false;

        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                retVal = false;
                loge("#execUpdate", e);
            }
        }
        return retVal;

    }

    /**
     * Execute raw SQL for update
     * 
     * @param modelObj
     * @param includeExcludeColumnNames
     * @return true:DB operation success false:failure
     */
    public boolean execUpdateByRawSQL(String updateRawSQL) {

        boolean retVal = false;

        log("#execUpdateByRawSQL updateRawSQL=" + updateRawSQL);

        final Connection conn = createConnection();

        try {

            Statement stmt = null;

            // There is a possibility that the error occurs in one single update
            // statement.
            // Therefore, turn the auto commit off.
            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            stmt.executeUpdate(updateRawSQL);

            // finally,commit
            conn.commit();

            retVal = true;

        } catch (SQLException e) {

            loge("#execUpdateByRawSQL", e);
            retVal = false;

        } finally {

            try {
                conn.close();
            } catch (SQLException e) {
                retVal = false;
                loge("#execUpdateByRawSQL", e);
            }
        }
        return retVal;

    }

    /**
     * Insert the specified model object into the DB
     * 
     * @param modelObjects
     * @return true:DB operation success false:failure
     */
    public boolean execInsert(D6Model[] modelObjects) {
        final D6Inex includeExcludeColumnNames = null;
        return execInsert(modelObjects, includeExcludeColumnNames);
    }

    /**
     * Insert the specified model object into the DB
     * 
     * @param modelObjects
     * @param includeExcludeColumnNames
     *            You can select either 'the column name you want to reflected
     *            in the database' AND 'the column name you don't want to
     *            reflect in the database'. When omitted (null specified)
     *            ,reflects all properties in the model class has to be
     *            reflected to the database.
     * 
     * @return true:DB operation success false:failure
     */
    public boolean execInsert(D6Model[] modelObjects, D6Inex includeExcludeColumnNames) {
        log("#execInsert");
        boolean retVal = false;

        if (modelObjects == null) {
            return retVal;
        }

        final int numOfModelObjects = modelObjects.length;

        if (numOfModelObjects == 0) {
            return retVal;
        }

        final D6Model firstModelObject = modelObjects[0];

        final D6CrudInsertHelper d6CrudInsertHelper = new D6CrudInsertHelper(firstModelObject.getClass());

        final String insertSQL = d6CrudInsertHelper.createInsertPreparedSQLStatement(includeExcludeColumnNames);

        Connection conn = null;

        try {

            PreparedStatement preparedStmt = null;

            conn = createConnection();

            // There is a possibility that the error occurs in one single insert
            // statement.
            // Therefore, turn the auto commit off.
            conn.setAutoCommit(false);

            preparedStmt = conn.prepareStatement(insertSQL);

            for (int i = 0; i < modelObjects.length; i++) {

                D6Model model = modelObjects[i];

                d6CrudInsertHelper.map(model, preparedStmt, includeExcludeColumnNames);

                // execute SQL
                preparedStmt.executeUpdate();

            }

            // finally commit
            conn.commit();

            retVal = true;

        } catch (SQLException e) {

            loge("#execInsert", e);
            retVal = false;

        } catch (D6Exception e) {

            // catch from helper
            loge("#execInsert", e);
            retVal = false;
        } finally {
            if (conn != null) {
                try {

                    conn.close();
                } catch (SQLException e) {
                    loge("#execInsert", e);
                    retVal = false;
                }
            }
        }
        return retVal;

    }

    // Methods_of_SELECT///////////////////////////////////////////////////
    /**
     * Returns the total number of the lines of rows corresponding to the
     * specified model class
     * 
     * @param modelClazz
     * @return true:DB operation success false:failure
     */
    public int execSelectCount(Class<? extends D6Model> modelClazz) {

        final D6CrudSelectHelper d6CrudSelectHelper = new D6CrudSelectHelper(modelClazz);
        final String sqlForSelectCount = d6CrudSelectHelper.getSQLForSelectCount();

        return execSelectCount(sqlForSelectCount);
    }

    /**
     * Execute the SQL for number search<br>
     * ex.SELECT COUNT(*) FROM table;
     * 
     * @param preparedSql
     * @param searchKeys
     * @return number of result
     */
    public int execSelectCount(String preparedSql) {
        return execSelectCount(preparedSql, null);
    }

    /**
     * Execute the SQL for number search<br>
     * 
     * @param preparedSql
     * @param searchKeys
     * @return number of result
     */
    public int execSelectCount(String preparedSql, Object[] searchKeys) {
        log("#execSelectCount preparedSql=" + preparedSql + " searchKeys=" + searchKeys);
        int retVal = 0;

        PreparedStatement preparedStmt = null;
        ResultSet rs = null;

        final Connection conn = createConnection();

        try {

            preparedStmt = conn.prepareStatement(preparedSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            final StringBuilder logSb = new StringBuilder();

            if (searchKeys != null) {
                logSb.append("/ ");
                for (int i = 0; i < searchKeys.length; i++) {

                    setObject((i + 1), preparedStmt, searchKeys[i]);

                    logSb.append("key(" + (i + 1) + ")=" + searchKeys[i]);
                    logSb.append(" ");
                }
            }

            log("#execSelectCount SQL=" + preparedSql + " " + logSb.toString());

            // execute SQL
            rs = preparedStmt.executeQuery();

            while (rs.next()) {
                retVal = rs.getInt(1);
            }

        } catch (Exception e) {
            loge("#execSelectCount", e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

                loge("#execSelectCount", e);
            }
        }
        return retVal;

    }

    /**
     * Execute select statement and returns the all lines of rows corresponding
     * to the specified model class as array of Objects
     * 
     * @param modelClazz
     * @return
     */
    public Object[] execSelectTable(Class<? extends D6Model> modelClazz) {

        final DBTable dbTable = modelClazz.getAnnotation(DBTable.class);
        final String dbTableName = dbTable.tableName();
        return execSelectTable("SELECT * FROM " + dbTableName, modelClazz);
    }

    public Object[] execSelectTable(Class<? extends D6Model> modelClazz, WhereCondition whereCondition) {
        return execSelectTable(modelClazz, whereCondition, null);
    }

    public Object[] execSelectTable(Class<? extends D6Model> modelClazz, WhereCondition whereCondition, Object[] searchKeys) {

        final DBTable dbTable = modelClazz.getAnnotation(DBTable.class);
        final String dbTableName = dbTable.tableName();

        final String preparedSql = "SELECT * FROM " + dbTableName + " " + whereCondition.toSql();

        final Map<Class<?>, List<Object>> result = execSelectTableWithJoin(preparedSql, searchKeys, modelClazz);

        final List<Object> rowList = result.get(modelClazz);

        return toArray(rowList, modelClazz);
    }

    /**
     * Execute select statement for the single table.
     * 
     * @param preparedSql
     * @param modelClazz
     * @return
     */
    public Object[] execSelectTable(String preparedSql, Class<? extends D6Model> modelClazz) {
        return execSelectTable(preparedSql, null, modelClazz);
    }

    /**
     * Execute select statement for the single table. <br>
     * <br>
     * -About SQL<br>
     * You can use prepared SQL.<br>
     * <br>
     * In addition,you can also use non-wildcard ('?') SQL (=raw SQL).In this
     * case searchKeys must be null or empty array(size 0 array).<br>
     * When you use a wildcard('?'), you must not include the "'"(=>single
     * quotes) to preparedSQL.<br>
     * 
     * <br>
     * -About processing<br>
     * Used when you execute the SQL that is JOIN multiple tables.<br>
     * 
     * In this method, you can specify more than one model class.<br>
     * 
     * When the column name specified in the annotation of the model classes is
     * included in the resultSet,<br>
     * a value corresponding to the column name is set to the corresponding
     * field of model objects.<br>
     * 
     * In other words, if multiple model class has the same column name, values
     * in the resultSet is set in the same manner for each mode class.<br>
     * 
     * <br>
     * 
     * @param preparedSql
     * @param searchKeys
     * @param modelClazz
     * @return
     */
    public Object[] execSelectTable(String preparedSql, Object[] searchKeys, Class<? extends D6Model> modelClazz) {

        @SuppressWarnings("unchecked")
        final Map<Class<?>, List<Object>> result = execSelectTableWithJoin(preparedSql, searchKeys, modelClazz);

        final List<Object> rowList = result.get(modelClazz);

        return toArray(rowList, modelClazz);
    }

    /**
     * 
     * Execute select statement for the joined multiple table.<br>
     * 
     * {@see #execSelectTableWithJoin(String, String[], Class...)}<br>
     * 
     * @param preparedSql
     * @param modelClazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<Class<?>, List<Object>> execSelectTableWithJoin(String preparedSql, Class<? extends D6Model>... modelClazz) {
        return execSelectTableWithJoin(preparedSql, null, modelClazz);
    }

    /**
     * Execute select statement for the joined multiple table.<br>
     * <br>
     * <br>
     * -About SQL<br>
     * You can use prepared SQL.<br>
     * <br>
     * In addition,you can also use non-wildcard ('?') SQL (=raw SQL).In this
     * case searchKeys must be null or empty array(size 0 array).<br>
     * When you use a wildcard('?'), you must not include the "'"(=>single
     * quotes) to preparedSQL.<br>
     * 
     * <br>
     * -About processing<br>
     * Used when you execute the SQL that is JOIN multiple tables.<br>
     * 
     * In this method, you can specify more than one model class.<br>
     * 
     * When the column name specified in the annotation of the model classes is
     * included in the resultSet,<br>
     * a value corresponding to the column name is set to the corresponding
     * field of model objects.<br>
     * 
     * In other words, if multiple model class has the same column name, values
     * in the resultSet is set in the same manner for each mode class.<br>
     * 
     * 
     * @param preparedSql
     * @param searchKeys
     *            If the prepared SQL includes a wild card (?), Here is list of
     *            the string to be substituted for wild card.
     * 
     *            The order of value to be included in the array must be the
     *            same as order of appearance of the wild card.
     * 
     * @param modelClazz
     *            More than one model class in a comma-separated manner for
     *            mapping the results
     * 
     * @return SQL execution result is returned as MAP. <br>
     *         MAP,key is the model class, value is of instance of the model
     *         class specified as key.
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<Class<?>, List<Object>> execSelectTableWithJoin(String preparedSql, Object[] searchKeys, Class<? extends D6Model>... modelClazz) {
        log("#execSelectTableWithJoin preparedSql=" + preparedSql + " searchKeys=" + searchKeys + " modelClazz=" + modelClazz);
        final Map<Class<?>, List<Object>> resultMap = new HashMap<Class<?>, List<Object>>();

        final List<ModelWrapper> modelList = new ArrayList<ModelWrapper>();

        for (int i = 0; i < modelClazz.length; i++) {

            @SuppressWarnings("unchecked")
            final ModelWrapper model = new ModelWrapper(modelClazz[i]);
            modelList.add(model);
        }

        PreparedStatement preparedStmt = null;
        ResultSet rs = null;

        final Connection conn = createConnection();

        try {

            preparedStmt = conn.prepareStatement(preparedSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            final StringBuilder logSb = new StringBuilder();
            if (searchKeys != null) {
                logSb.append("/ ");
                for (int i = 0; i < searchKeys.length; i++) {
                    //
                    Object object = searchKeys[i];

                    setObject((i + 1), preparedStmt, object);

                    logSb.append("key(" + (i + 1) + ")=" + searchKeys[i]);
                    logSb.append(" ");
                }
            }

            log("#execSelectTableWithJoin SQL=" + preparedSql + " " + logSb.toString());

            // execute SQL
            rs = preparedStmt.executeQuery();

            final ResultSetMetaData rsMetaData = rs.getMetaData();
            final int numberOfColumns = rsMetaData.getColumnCount();
            final List<String> columnNameList = new ArrayList<String>();

            // cache column names of this result set
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = rsMetaData.getColumnName(i + 1);
                columnNameList.add(columnName);
            }

            while (rs.next()) {

                // Processing of a single resultset[begin]=============

                for (int i = 0; i < numberOfColumns; i++) {

                    // Get from the current resultSet
                    final String columnName = columnNameList.get(i);

                    final Object value = rs.getObject(i + 1);

                    // Set the values to all the properties of model class (You
                    // know property is corresponding to each column of the DB)
                    // via modelWrapper
                    for (ModelWrapper model : modelList) {
                        // set value to model wrapper
                        model.setValue(columnName, value);
                    }
                }

                // Processing of a single resultset[end]=============

                for (ModelWrapper model : modelList) {

                    final Class<?> modelClazzName = model.getClazz();

                    List<Object> modelObjectList = resultMap.get(modelClazzName);

                    // Generate the result list corresponding to a certain model
                    // class if the list have not been generated.
                    if (modelObjectList == null) {
                        modelObjectList = new ArrayList<Object>();
                        resultMap.put(modelClazzName, modelObjectList);
                    }

                    // Generates a model object having a property value held in
                    // the model wrapper, and stores the model object in the
                    // modelObjectList
                    final Object resultModelObject = model.getAsObject();
                    modelObjectList.add(resultModelObject);

                    model.initializeFieldMap();
                }

            }

        } catch (Exception e) {
            loge("#execSelectTableWithJoin General ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                loge("#execSelectTableWithJoin SQLException ", e);
            }
        }

        return resultMap;

    }

    /**
     * Set object to the preparedStatement
     * 
     * @param parameterIndex
     * @param preparedStmt
     * @param value
     * @throws SQLException
     */
    private void setObject(int parameterIndex, PreparedStatement preparedStmt, Object value) throws SQLException {

        preparedStmt.setObject(parameterIndex, value);
    }

    /**
     * returns Object array from Object List
     * 
     * @param objectList
     * @param modelClazz
     * @return
     */
    private Object[] toArray(List<Object> objectList, Class<? extends D6Model> modelClazz) {

        if (objectList == null) {
            return (Object[]) Array.newInstance(modelClazz, 0);
        }

        final Object[] resultObjects = objectList.toArray((Object[]) Array.newInstance(modelClazz, 0));

        return resultObjects;
    }

    /**
     * convert result into ModelObject from the result of execSelectWithJoin
     * 
     * @param o
     * @param modelClazz
     * @return
     */
    public Object[] getAsModel(Map<Class<?>, List<Object>> o, Class<? extends D6Model> modelClazz) {

        return toArray(o.get(modelClazz), modelClazz);
    }

    /**
     * Get the DB connection
     * 
     * @return
     */
    private Connection createConnection() {
        DBConnCreator dbConnCreator = new DBConnCreator(mConnInfo);
        Connection conn = dbConnCreator.createDBConnection();
        return conn;
    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {

        D6Logger.loge(this.getClass(), msg, e);
    }
}
