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

import java.util.List;

import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.annotation.DBTable;

/**
 * Helper class to perform the following using the model classes<br>
 * -Have utility method for SELECT statement<br>
 * <br>
 * Called from D6Crud
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6CrudSelectHelper extends D6CrudHelperBase {

    public D6CrudSelectHelper(Class<? extends D6Model> modelClazz) {
        super(modelClazz);
    }

    /**
     * Returns SQL for 'Select Count'<br>
     * like SELECT COUNT(PrimaryKey)<br>
     * Because primary key is indexed by RDBMS , it can respond at a high speed
     * for a request.
     * 
     * @param modelClazz
     * @return
     */
    String getSQLForSelectCount() {

        final DBTable dbTable = mModelClazz.getAnnotation(DBTable.class);
        final String dbTableName = dbTable.tableName();
        final List<DBColumn> primaryKeyList = getPrimaryKeyColumnList();
        final DBColumn primaryKey = primaryKeyList.get(0);
        final String primaryKeyColumnName = primaryKey.columnName();

        final String preparedSql = "SELECT COUNT(" + primaryKeyColumnName + ") FROM " + dbTableName;

        return preparedSql;
    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {
        D6Logger.loge(this.getClass(), msg, e);
    }
}
