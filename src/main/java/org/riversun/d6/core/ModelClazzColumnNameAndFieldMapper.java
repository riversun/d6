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
import java.util.LinkedHashMap;
import java.util.Map;

import org.riversun.d6.annotation.DBColumn;

/**
 *
 * To generate the Map which key is columnName and value is model class's field
 * info from D6 Model class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class ModelClazzColumnNameAndFieldMapper {

    private final Class<?> mModelClazz;
    private final Map<String, D6ModelClassFieldInfo> mFieldMap;

    public ModelClazzColumnNameAndFieldMapper(Class<?> modelClazz) {
        mModelClazz = modelClazz;
        mFieldMap = new LinkedHashMap<String, D6ModelClassFieldInfo>();
    }

    public void rebuild(Map<String, D6ModelClassFieldInfo> map) {
        build(map);
    }

    public Map<String, D6ModelClassFieldInfo> build() {
        build(mFieldMap);
        return mFieldMap;
    }

    /**
     * To populate FieldMap(key is columnName,value is fieldInfo) holding the
     * column and fields
     * 
     * @param refFieldMap
     *            reference of non-null field map
     * @return
     */
    private void build(Map<String, D6ModelClassFieldInfo> refFieldMap) {

        refFieldMap.clear();

        final Field[] fields = mModelClazz.getFields();

        for (int i = 0; i < fields.length; i++) {

            final Field field = fields[i];

            final DBColumn annoColumn = field.getAnnotation(DBColumn.class);

            final String columnName = annoColumn.columnName();
            final String columnType = annoColumn.columnType();

            if (columnName == null || columnType == null) {
                continue;
            }

            final D6ModelClassFieldInfo fieldInfo = new D6ModelClassFieldInfo();

            //
            fieldInfo.field = field;
            fieldInfo.columnName = columnName;
            fieldInfo.columnType = columnType;
            fieldInfo.value = null;
            //
            fieldInfo.isAutoIncrement = annoColumn.isAutoIncrement();
            fieldInfo.isNullable = annoColumn.isNullable();
            fieldInfo.isPrimaryKey = annoColumn.isPrimaryKey();
            fieldInfo.isUnique = annoColumn.isUnique();

            refFieldMap.put(columnName, fieldInfo);
        }

    }
}
