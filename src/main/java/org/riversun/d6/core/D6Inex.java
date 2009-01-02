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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * Select columns to Inlude / Exclude
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6Inex {

    private final List<String> mIncludeColumnNameList = new ArrayList<String>();
    private final List<String> mExcludeColumnNameList = new ArrayList<String>();

    /**
     * Add a column name to be included in the search condition
     * 
     * @param columnName
     * @return
     */
    public D6Inex addIncludeColumn(String columnName) {
        mIncludeColumnNameList.add(columnName);
        return D6Inex.this;
    }

    /**
     * Add a column name to be excluded from the search condition
     * 
     * @param columnName
     * @return
     */
    public D6Inex addExcludeColumn(String columnName) {
        mExcludeColumnNameList.add(columnName);
        return D6Inex.this;
    }

    /**
     * Clear include/exclude column names added
     */
    public void clear() {
        mIncludeColumnNameList.clear();
        mExcludeColumnNameList.clear();
    }

    /**
     * Edit(mainly remove columnName) column collection to fit the
     * Include/Exclude status
     * 
     * @param columnCollection
     */
    public void manipulate(Collection<String> columnCollection) {

        final List<String> cloneOfColumnCollection = new ArrayList<String>(columnCollection);

        if (mIncludeColumnNameList.size() > 0 && mExcludeColumnNameList.size() > 0) {
            throw new RuntimeException("You cannot set both include and exclude column names.");
        }

        if (mIncludeColumnNameList.size() > 0) {

            log("#manipulate include mode includeColumnNameList=" + mIncludeColumnNameList);
            // include mode
            for (String columnName : cloneOfColumnCollection) {

                if (mIncludeColumnNameList.contains(columnName)) {
                    // if contain, ok
                    log("#manipulate include mode columnName '" + columnName + "' hold");
                } else {
                    // if not contain,then delete
                    columnCollection.remove(columnName);
                }
            }
        }

        if (mExcludeColumnNameList.size() > 0) {
            // exclude mode
            log("#manipulate exclude mode excludeColumnNameList=" + mExcludeColumnNameList);
            for (String excludeColumnName : mExcludeColumnNameList) {

                if (cloneOfColumnCollection.contains(excludeColumnName)) {
                    columnCollection.remove(excludeColumnName);
                    log("#manipulate exclude mode columnName '" + excludeColumnName + "' skipped");
                } else {

                }
            }
        }
    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {

        D6Logger.loge(this.getClass(), msg, e);
    }

}