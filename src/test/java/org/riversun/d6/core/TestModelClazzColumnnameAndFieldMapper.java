package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 
 * Test for TestModelClazzColumnnameAndFieldMapper class
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestModelClazzColumnnameAndFieldMapper {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_build() throws Exception {

        final ModelClazzColumnNameAndFieldMapper modelClazzColumnNameAndFieldMapper = new ModelClazzColumnNameAndFieldMapper(org.riversun.d6.test.model.School.class);
        final Map<String, D6ModelClassFieldInfo> fieldMap = modelClazzColumnNameAndFieldMapper.build();

        final String fieldInfo1Str = "D6ModelClassFieldInfo [columnName=school_id, columnType=tinyint(4), isNullable=false, isPrimaryKey=true, isAutoIncrement=false, isUnique=false, field=public java.lang.Integer org.riversun.d6.test.model.School.schoolId]";
        final String fieldInfo2Str = "D6ModelClassFieldInfo [columnName=school_name, columnType=varchar(255), isNullable=false, isPrimaryKey=true, isAutoIncrement=false, isUnique=false, field=public java.lang.String org.riversun.d6.test.model.School.schoolName]";
        final String fieldInfo3Str = "D6ModelClassFieldInfo [columnName=school_desc, columnType=text, isNullable=true, isPrimaryKey=false, isAutoIncrement=false, isUnique=false, field=public java.lang.String org.riversun.d6.test.model.School.schoolDesc]";

        assertEquals(fieldInfo1Str, fieldMap.get("school_id").toString());
        assertEquals(fieldInfo2Str, fieldMap.get("school_name").toString());
        assertEquals(fieldInfo3Str, fieldMap.get("school_desc").toString());

    }

}