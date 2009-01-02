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
package org.riversun.d6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * class for creating Database Connection by DriverManager
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class DBConnCreator {

    private DBConnInfo mDBConnInfo;

    /**
     * 
     * @param connInfo
     */
    public DBConnCreator(DBConnInfo connInfo) {
        mDBConnInfo = connInfo;
    }

    /**
     * get connection object from driver manager
     * 
     * @return
     */
    public Connection createDBConnection() {
        if (mDBConnInfo != null) {
            return getConnectionViaDriverMangaer(mDBConnInfo.DBDriver, mDBConnInfo.DBUrl, mDBConnInfo.DBUser, mDBConnInfo.DBPassword);
        } else {
            return null;
        }
    }

    /**
     * get connection object from driver manager
     * 
     * @param url
     * @param user
     * @param password
     * @return
     */
    private Connection getConnectionViaDriverMangaer(String driver, String url, String user, String password) {

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
