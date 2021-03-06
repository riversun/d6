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
import java.util.List;

/**
 * 
 * Field information of the model class
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
class D6ModelClassFieldInfo {
    /**
     * Column name of the DB table
     */
    public String columnName;
    /**
     * Column type of the DB table
     */
    public String columnType;

    /**
     * Is column type nullable
     */
    public boolean isNullable;

    /**
     * is column primary key
     */
    public boolean isPrimaryKey;

    /**
     * is column auto increment
     */
    public boolean isAutoIncrement;

    /**
     * is column unique
     */
    public boolean isUnique;

    /**
     * Field of the model class
     */
    public Field field;

    /**
     * Field value of the model class
     */
    Object value;

    /**
     * RDMS specific composit type values
     */
    List<Object> valuesForSpecialType;

    @Override
    public String toString() {
        return "D6ModelClassFieldInfo [columnName=" + columnName + ", columnType=" + columnType + ", isNullable=" + isNullable + ", isPrimaryKey=" + isPrimaryKey + ", isAutoIncrement="
                + isAutoIncrement + ", isUnique=" + isUnique + ", field=" + field + "]";
    }

}