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

import java.util.Calendar;

/**
 * Helper class for converting form java.util.Date to
 * java.sql.Date/java.sql.Time/java.sql.TimeStamp
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class D6DateUtil {

    /**
     * Get SQL Date(java.sql.Date) from supplied parameters
     * 
     * @param month
     * @param date
     * @return
     */
    public static java.sql.Date getSqlDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(0));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return getSqlDate(cal.getTime());
    }

    /**
     * Get SQL Date(java.sql.Date) from java.util.Date
     * 
     * @param date
     * @return
     */
    public static java.sql.Date getSqlDate(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * Get SQL Time(java.sql.Time) from supplied parameters
     * 
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static java.sql.Time getSqlTime(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(0));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);

        return getSqlTime(cal.getTime());
    }

    /**
     * Get java.sql.Time from java.util.Date
     * 
     * @param date
     * @return
     */
    public static java.sql.Time getSqlTime(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new java.sql.Time(cal.getTimeInMillis());
    }

    /**
     * Get java.sql.Timestamp from java.util.Date
     * 
     * @param date
     * @return
     */
    public static java.sql.Timestamp getSqlTimeStamp(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

}
