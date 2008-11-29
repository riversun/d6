package org.riversun.d6.predev.model;

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
import org.riversun.string_grabber.StringGrabber;

/**
 * 
 * Column Info for pre develop
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class D6ColumnMetaInfo {
	public String columnName;
	public String typeName;
	public int columnSize;
	public boolean isPrimaryKey;
	public int primaryKeySeqNum;
	public String pkName;
	public boolean isNullable;
	public boolean isAutoIncrement;
	public int dataType;
	public String className;

	public String toDefString() {
		StringGrabber sg = new StringGrabber();
		sg.append(columnName);
		sg.append(" ");
		sg.append(typeName);
		if (columnSize > 0) {
			sg.append("(").append(columnSize + "").append(")");
		}
		sg.append(" ");

		if (isAutoIncrement) {
			sg.append("AUTO_INCREMENT");
			sg.append(" ");
		}
		if (isPrimaryKey) {
			sg.append("PRIMARY KEY");
			sg.append(" ");
		}
		if (isNullable == false) {
			sg.append("NOT NULL");
			sg.append("");
		}

		return sg.toString();
	}

	@Override
	public String toString() {
		return "D6ColumnMetaInfo [columnName=" + columnName + ", typeName=" + typeName + ", columnSize=" + columnSize + ", isPrimaryKey=" + isPrimaryKey + ", isNullable=" + isNullable
				+ ", primaryKeySeqNum=" + primaryKeySeqNum + ", isAutoIncrement=" + isAutoIncrement + ", dataType=" + dataType + ", className=" + className + "]";
	}

}
