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
package org.riversun.d6.predev;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.riversun.d6.DBConnCreator;
import org.riversun.d6.DBConnInfo;
import org.riversun.d6.core.D6Crud;
import org.riversun.d6.core.D6Logger;
import org.riversun.d6.predev.model.D6ColumnMetaInfo;
import org.riversun.d6.predev.model.D6TableMetaInfo;

/**
 * Utility class to show Database Tables and Columns
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class ShowTableMetaData {

	public static void main(String[] args) {

		D6Logger.setLogging(false);

		ShowTableMetaData obj = new ShowTableMetaData();

		final String H2_DB_FILE_PATH_ABS = "file:C:/temp2/sample2";

		final String JDBC_DRIVER = "org.h2.Driver";
		final String DATABASE_URL = "jdbc:h2:tcp://localhost/" + H2_DB_FILE_PATH_ABS;
		final String USER = "sa";
		final String PASS = "";

		final DBConnInfo dbConnInfo = new DBConnInfo();
		dbConnInfo.DBDriver = JDBC_DRIVER;
		dbConnInfo.DBUrl = DATABASE_URL;
		dbConnInfo.DBUser = USER;
		dbConnInfo.DBPassword = PASS;

		final D6Crud crud = new D6Crud(dbConnInfo);

		crud.execUpdateByRawSQL("drop table if exists session");
		String sql = "create table if not exists session("
				+ "id bigint auto_increment PRIMARY KEY NOT NULL,"
				+ "device_id varchar(255) NOT NULL,"
				+ "session_id varchar(255) NOT NULL,"
				+ "reg_id varchar(255) NOT NULL)";

		crud.execUpdateByRawSQL(sql);

		final List<D6TableMetaInfo> tables = obj.getTablesInfo(dbConnInfo);

		for (D6TableMetaInfo tbl : tables) {
			System.out.println("---------------------------------");
			System.out.println("TABLE:" + tbl.tableName);
			List<D6ColumnMetaInfo> colList = tbl.columnList;

			for (D6ColumnMetaInfo col : colList) {
				System.out.println(" " + col.toDefString() + ",");
			}

		}
	}

	/**
	 * Get table definition from db by retrieving the db meta data
	 * 
	 * @param dbConnInfo
	 * @return
	 */
	public List<D6TableMetaInfo> getTablesInfo(DBConnInfo dbConnInfo) {

		final List<D6TableMetaInfo> retTablesInfo = new ArrayList<D6TableMetaInfo>();

		final Connection conn = new DBConnCreator(dbConnInfo).createDBConnection();

		final List<String> tableList = new ArrayList<String>();

		ResultSet rs = null;

		try {
			final DatabaseMetaData metaData = conn.getMetaData();

			rs = metaData.getTables(null, null, "%", new String[] { "TABLE" });

			try {
				while (rs.next()) {
					final String tableName = rs.getString("TABLE_NAME");
					tableList.add(tableName);
				}
			} finally {
				rs.close();
			}

			for (String tableName : tableList) {

				final D6TableMetaInfo tableInfo = new D6TableMetaInfo();

				tableInfo.tableName = tableName;

				final Map<String, D6ColumnMetaInfo> colInfoMap = new LinkedHashMap<String, D6ColumnMetaInfo>();

				rs = metaData.getColumns(null, null, tableName, null);

				try {
					while (rs.next()) {
						D6ColumnMetaInfo colMeta = new D6ColumnMetaInfo();
						colMeta.columnName = rs.getString("COLUMN_NAME");
						colMeta.typeName = rs.getString("TYPE_NAME");
						colMeta.columnSize = rs.getInt("COLUMN_SIZE");
						colMeta.isNullable = "YES".equals(rs.getString("IS_NULLABLE"));
						colMeta.isAutoIncrement = "YES".equals("IS_AUTOINCREMENT");
						// java.sql.Types
						colMeta.dataType = rs.getInt("DATA_TYPE");

						colInfoMap.put(colMeta.columnName, colMeta);
					}
				} finally {
					rs.close();
				}

				rs = metaData.getPrimaryKeys(null, null, tableName);

				try {
					while (rs.next()) {
						final String columnName = rs.getString("COLUMN_NAME");
						final String pkName = rs.getString("PK_NAME");
						final short primaryKeySequentialIndex = rs.getShort("KEY_SEQ");

						final D6ColumnMetaInfo colMeta = colInfoMap.get(columnName);
						if (colMeta != null) {
							colMeta.pkName = pkName;
							colMeta.isPrimaryKey = true;
							colMeta.primaryKeySeqNum = primaryKeySequentialIndex;
						}

					}
				} finally {
					rs.close();
				}

				final Statement stmt = conn.createStatement();

				try {

					rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE 0=1");
					try {
						ResultSetMetaData rmd = rs.getMetaData();
						for (int i = 1; i <= rmd.getColumnCount(); i++) {
							String columnName = rmd.getColumnName(i);
							final D6ColumnMetaInfo colMeta = colInfoMap.get(columnName);
							if (colMeta != null) {
								colMeta.className = rmd.getColumnClassName(i);
							}

						}
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}

				for (String columnName : colInfoMap.keySet()) {
					D6ColumnMetaInfo colMeta = colInfoMap.get(columnName);
					tableInfo.columnList.add(colMeta);
				}

				retTablesInfo.add(tableInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		return retTablesInfo;
	}

}
