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

import org.riversun.string_grabber.StringGrabber;

/**
 * 
 * Where condition builder
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class WhereCondition {

    public WhereCondition() {
    }

    private StringGrabber mSgSQL = new StringGrabber();

    /**
     * Method of adding SQL 'token' in the WhereCondition class
     */
    private enum ESqlToken {

        COLUMN, // added column name
        OPERATOR, // added operation like equals,less than,more than,etc.
        VALUE, // added column value
        CONDITION, // added condition like 'AND' 'OR'
        WHERE, // added whereCondition itself
        NOTHING, // initial state

    }

    /**
     * Command executed in the preceding operation
     */
    private ESqlToken mPreviousToken = ESqlToken.NOTHING;

    /**
     * Add WhereCondition object itself surrounded with bracket into where
     * clause
     * 
     * @param whereCondition
     * @return
     */
    public WhereCondition WHERE(WhereCondition whereCondition) {
        checkTokenOrderIsCorrect(ESqlToken.WHERE);
        mSgSQL.append("(");
        mSgSQL.append(whereCondition.toWhereConditionPart());
        mSgSQL.append(")");
        return WhereCondition.this;
    }

    /**
     * Add 'AND' condition into WHERE clause
     * 
     * @return
     */
    public WhereCondition AND() {

        mSgSQL.append(" AND ");
        checkTokenOrderIsCorrect(ESqlToken.CONDITION);
        return WhereCondition.this;
    }

    /**
     * Add 'OR' condition into WHERE clause
     * 
     * @return
     */
    public WhereCondition OR() {

        mSgSQL.append(" OR ");
        checkTokenOrderIsCorrect(ESqlToken.CONDITION);
        return WhereCondition.this;
    }

    /**
     * Add columnName into WHERE clause
     * 
     * @param columnName
     * @return
     */
    public WhereCondition Col(String columnName) {

        mSgSQL.append(columnName);
        checkTokenOrderIsCorrect(ESqlToken.COLUMN);
        return WhereCondition.this;
    }

    /**
     * Add '=' operator into WHERE clause
     * 
     * @return
     */
    public WhereCondition Equals() {
        mSgSQL.append("=");
        checkTokenOrderIsCorrect(ESqlToken.OPERATOR);
        return WhereCondition.this;
    }

    /**
     * Add '>' operator into WHERE clause
     * 
     * @return
     */
    public WhereCondition MoreThan() {
        mSgSQL.append(">");
        checkTokenOrderIsCorrect(ESqlToken.OPERATOR);
        return WhereCondition.this;
    }

    /**
     * Add '>=' operator into WHERE clause
     * 
     * @return
     */
    public WhereCondition MoreEqualsThan() {
        mSgSQL.append(">=");
        checkTokenOrderIsCorrect(ESqlToken.OPERATOR);
        return WhereCondition.this;
    }

/**
     * Add '<' operator into WHERE clause
     * @return
     */
    public WhereCondition LessThan() {
        mSgSQL.append("<");
        checkTokenOrderIsCorrect(ESqlToken.OPERATOR);
        return WhereCondition.this;
    }

    /**
     * Add '<=' operator into WHERE clause
     * 
     * @return
     */
    public WhereCondition LessEqualsThan() {
        mSgSQL.append("<=");
        checkTokenOrderIsCorrect(ESqlToken.OPERATOR);
        return WhereCondition.this;
    }

    /**
     * Add value of column into WHERE clause
     * 
     * @param val
     * @return
     */
    public WhereCondition Val(Object val) {

        if ("?".equals(val)) {
            // question mark means WILDCARD for prepared statement
            mSgSQL.append(" ?");
        }
        else {
            mSgSQL.append("'");
            mSgSQL.append(val.toString());
            mSgSQL.append("'");
        }

        checkTokenOrderIsCorrect(ESqlToken.VALUE);
        return WhereCondition.this;
    }

    /**
     * Add value as wildcard
     */
    public WhereCondition ValWildCard() {

        return Val("?");
    }

    /**
     * Confirm whether the token is built in the correct order, and throws an
     * exception if there is a problem.
     * 
     * @param tokenTobeAdded
     */
    private void checkTokenOrderIsCorrect(ESqlToken tokenTobeAdded) {

        switch (tokenTobeAdded) {

        case COLUMN:
            if (mPreviousToken != ESqlToken.CONDITION && mPreviousToken != ESqlToken.NOTHING) {
                throw new D6RuntimeException("AND or OR method is required at here." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
            }
            break;

        case OPERATOR:
            if (mPreviousToken != ESqlToken.COLUMN) {
                throw new D6RuntimeException("VALUE is required at here." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
            }
            break;

        case VALUE:
            if (mPreviousToken != ESqlToken.OPERATOR) {
                throw new D6RuntimeException("COLUMN or PROPERTY method is required at here." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
            }
            break;

        case CONDITION:
            if (mPreviousToken != ESqlToken.VALUE && mPreviousToken != ESqlToken.WHERE) {
                throw new D6RuntimeException("VALUE is required at here." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
            }
            break;

        case WHERE:
            if (mPreviousToken != ESqlToken.CONDITION && mPreviousToken != ESqlToken.NOTHING) {
                throw new D6RuntimeException("CONDITION is required at here." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
            }
            break;
        default:
        }

        mPreviousToken = tokenTobeAdded;
    }

    /**
     * where condition part
     * 
     * @return
     */
    String toWhereConditionPart() {
        if (mPreviousToken != ESqlToken.VALUE && mPreviousToken != ESqlToken.WHERE) {
            throw new D6RuntimeException("Where condition is not completed." + " '" + mSgSQL.toString() + "'<==error occurred around here.");
        }
        return mSgSQL.toString();
    }

    /**
     * Returns generated SQL
     * 
     * @return
     */
    String toSql() {

        return "WHERE " + toWhereConditionPart();
    }

}
