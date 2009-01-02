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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple logger for D6
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class D6Logger {

    public static boolean DEBUG = false;

    /**
     * To enable logging
     * 
     * @param enableLogging
     */
    public static void setLogging(boolean enableLogging) {
        DEBUG = enableLogging;
    }

    private static final SimpleDateFormat sSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * To output the standard log message to the standard out
     * 
     * @param clazz
     * @param msg
     */
    public static final void log(Class<?> clazz, String msg) {
        if (DEBUG) {
            System.out.println("[" + sSdf.format(new Date()) + "]" + "-" + "[" + clazz.getSimpleName() + "] " + msg);
        }
    }

    /**
     * To output the error log message to the error out
     * 
     * @param clazz
     * @param msg
     * @param e
     */
    public static final void loge(Class<?> clazz, String msg, Exception... e) {
        if (DEBUG) {

            String exceptionStr = "";

            if (e != null && e.length == 1) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e[0].printStackTrace(pw);
                pw.flush();
                exceptionStr = "exception = " + sw.toString();
            }
            System.err.println("[" + sSdf.format(new Date()) + "]" + "-" + "[" + clazz.getSimpleName() + "] " + msg + " " + exceptionStr);
        }
    }
}
