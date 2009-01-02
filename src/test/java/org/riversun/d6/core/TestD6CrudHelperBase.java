package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.test.model.School;

/**
 * 
 * Test for Model Wrapper
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestD6CrudHelperBase {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    static final class HelperClass extends D6CrudHelperBase {

        public HelperClass(Class<? extends D6Model> modelClazz) {
            super(modelClazz);
        }

        @Override
        void log(String msg) {
        }

        @Override
        void loge(String msg, Exception... e) {
        }

    }

    @Test
    public void test_getFieldInfo() throws Exception {

        final HelperClass obj = new HelperClass(School.class);
        final D6ModelClassFieldInfo fieldInfo = obj.getFieldInfo("school_id");
        final String expected = "D6ModelClassFieldInfo [columnName=school_id, columnType=tinyint(4), isNullable=false, isPrimaryKey=true, isAutoIncrement=false, isUnique=false, field=public java.lang.Integer org.riversun.d6.test.model.School.schoolId]";

        assertEquals(expected, fieldInfo.toString());

    }

    @Test
    public void test_getAllColumnNames() throws Exception {
        final HelperClass obj = new HelperClass(School.class);
        final String[] colmnNames = new String[] {
                "school_id", "school_name", "school_desc"
        };

        final Set<String> allColumnNames = obj.getAllColumnNames();

        for (String columnName : colmnNames) {
            assertTrue(allColumnNames.contains(columnName));
        }

    }

    @Test
    public void test_getPrimaryColumnNames() throws Exception {
        final HelperClass obj = new HelperClass(School.class);
        final String[] colmnNames = new String[] {
                "school_id", "school_name"
        };

        final Set<String> primaryColumnNames = obj.getPrimaryColumnNames();

        for (String columnName : colmnNames) {
            assertTrue(primaryColumnNames.contains(columnName));
        }

    }

    @Test
    public void test_setValue() throws Exception {
        ;
    }

    @Test
    public void test_getPrimaryKeyFieldList() throws Exception {

        final HelperClass obj = new HelperClass(School.class);
        final String[] expectedFieldNames = new String[] {
                "schoolId", "schoolName"
        };

        final List<Field> primaryKeyFieldList = obj.getPrimaryKeyFieldList();
        final List<String> fieldNameList = new ArrayList<String>();

        for (Field primaryKeyField : primaryKeyFieldList) {
            fieldNameList.add(primaryKeyField.getName());
        }

        for (String fieldName : expectedFieldNames) {
            assertTrue(fieldNameList.contains(fieldName));
        }

    }

    @Test
    public void test_getPrimaryKeyColumnList() throws Exception {

        final HelperClass obj = new HelperClass(School.class);
        final String[] expectedColumnNames = new String[] {
                "school_id", "school_name"
        };

        final List<DBColumn> primaryKeyColumnList = obj.getPrimaryKeyColumnList();

        final List<String> columnNameList = new ArrayList<String>();

        for (DBColumn primaryKeyDbColumn : primaryKeyColumnList) {
            columnNameList.add(primaryKeyDbColumn.columnName());
        }

        for (String columnName : expectedColumnNames) {
            assertTrue(columnNameList.contains(columnName));
        }

    }

}