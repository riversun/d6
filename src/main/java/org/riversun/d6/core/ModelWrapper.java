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
import java.util.Map;
import java.util.Set;

/**
 * 
 * The helper class to set the obtained value as resultSet to Model class with
 * annotation of DB column name (=DBColumn)
 * 
 * <br>
 * USAGE<br>
 * <br>
 * specify column name by 'setValue' method and set the value that is obtained
 * as resultSet.<br>
 *
 * After setting the column of all (or of required columns),<br>
 * When 'getAsObject' method called,<br>
 * Model object is instanciated and values are mapped to the model object.
 * 
 *
 * @param <T>
 * @author Tom Misawa (riversun.org@gmail.com)
 */
class ModelWrapper<T> {

    private final Class<T> modelClazz;
    private final Map<String, D6ModelClassFieldInfo> mFieldMap;

    /**
     * 
     * @param model
     *            class
     */
    public ModelWrapper(Class<T> modelClazz) {
        this.modelClazz = modelClazz;

        mFieldMap = new ModelClazzColumnNameAndFieldMapper(modelClazz).build();
    }

    /**
     * Set the value to a field of the model object.The field is associated with
     * DB column name by DBColumn annotation.
     * 
     * @param columnName
     * @param value
     */
    public void setValue(String columnName, Object value) {

        D6ModelClassFieldInfo fieldInfo = mFieldMap.get(columnName);

        if (fieldInfo != null) {

            fieldInfo.value = value;

        }

    }

    /**
     * Get the model object populated with the value of the DB search results
     * 
     * @return
     */
    public T getAsObject() {
        try {
            T modelClassObj = modelClazz.newInstance();

            Set<String> columnNameSet = mFieldMap.keySet();

            for (String columnName : columnNameSet) {

                D6ModelClassFieldInfo fieldInfo = mFieldMap.get(columnName);

                final Field field = fieldInfo.field;

                final Object value = fieldInfo.value;

                if (value != null) {

                    // try to set 'null' if available
                    try {
                        field.set(modelClassObj, null);
                    } catch (Exception e) {
                    }

                    try {

                        field.set(modelClassObj, value);
                    } catch (IllegalAccessException e) {
                        // handling this exception for field.set(o, value);
                        loge("#getAsObject", e);
                    } catch (IllegalArgumentException e) {

                        // e.printStackTrace();
                        final String name = field.getName();
                        final Class<?> type = field.getType();

                        String msg = "The value of '" + columnName + "'=" + value + "(" + value.getClass() + ") couldn't set to variable '" + name + "'(" + type + ")";

                        loge("#getAsObject " + msg);

                    }
                }

            }
            return modelClassObj;
        } catch (IllegalAccessException e) {
            // handling this exception for modelClazz.newInstance();
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Initialize the FieldMap holding the column and field
     */
    public void initializeFieldMap() {

        new ModelClazzColumnNameAndFieldMapper(modelClazz).rebuild(mFieldMap);

    }

    /**
     * Returns the model class of this wrapper class is wrapping
     * 
     * @return
     */
    public Class<?> getClazz() {
        return modelClazz;
    }

    void log(String msg) {
        D6Logger.log(this.getClass(), msg);
    }

    void loge(String msg, Exception... e) {
        D6Logger.loge(this.getClass(), msg, e);
    }
}
