package org.riversun.d6.core;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.annotation.DBTable;

/**
 * 
 * Test for Model Wrapper
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestModelWrapper {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @DBTable(tableName = "test_table")
    static class TestModel
    {
        @DBColumn(columnName = "user_id", columnType = "tinyint(4)")
        public Integer userId;

        @DBColumn(columnName = "user_name", columnType = "varchar(255)")
        public String userName;
    }

    @Test
    public void test_setValue() throws Exception {

        final ModelWrapper<TestModel> modelWrapper = new ModelWrapper<TestModel>(TestModel.class);

        TestModel modelObj = null;

        // ordinary case
        modelWrapper.setValue("user_id", 1);
        modelObj = (TestModel) modelWrapper.getAsObject();
        Integer expected = 1;
        assertEquals(expected, modelObj.userId);

        // error case
        modelWrapper.setValue("user_id", "invalid_string");
        modelObj = (TestModel) modelWrapper.getAsObject();
        assertEquals(null, modelObj.userId);

    }

}