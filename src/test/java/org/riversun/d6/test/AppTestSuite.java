package org.riversun.d6.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.riversun.d6.core.TestD6Crud;
import org.riversun.d6.core.TestD6CrudDeleteHelper;
import org.riversun.d6.core.TestD6CrudHelperBase;
import org.riversun.d6.core.TestD6CrudInsertHelper;
import org.riversun.d6.core.TestD6CrudSelectHelper;
import org.riversun.d6.core.TestD6CrudUpdateHelper;
import org.riversun.d6.core.TestD6DateUtil;
import org.riversun.d6.core.TestD6Inex;
import org.riversun.d6.core.TestModelClazzColumnnameAndFieldMapper;
import org.riversun.d6.core.TestModelWrapper;
import org.riversun.d6.core.TestWhereCondition;

/**
 * 
 * Test Suite for JUNIT4
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
        TestD6Crud.class,// done
        TestD6CrudInsertHelper.class,// done
        TestD6CrudUpdateHelper.class,// done
        TestModelWrapper.class,// done
        TestWhereCondition.class,// done
        TestD6Inex.class,// done
        TestD6DateUtil.class,// done
        TestModelClazzColumnnameAndFieldMapper.class,// done
        TestD6CrudHelperBase.class,// done
        TestD6CrudSelectHelper.class,// done
        TestD6CrudDeleteHelper.class,// done
})
public class AppTestSuite {

}
