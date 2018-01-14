# Overview
'd6' is a java library for Lightweight O/R mapper with ease of use 

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.riversun/d6/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.riversun/d6)

It is licensed under [MIT License](http://opensource.org/licenses/MIT).


# Quick Start

**Step1.Create Database on MySQL 5.7**

```
show databases;

create database if not exists test_db;

use test_db;

CREATE TABLE article ( 
 entry_id INT NOT NULL PRIMARY KEY ,
 site_id INT NOT NULL ,
 published_at TIMESTAMP ,
 updated_at TIMESTAMP ,
 title VARCHAR(255) NULL ,
 link TEXT NULL COMMENT ,
 description TEXT NULL ,
 content TEXT NULL ,
 fulltext_col TEXT AS (CONCAT(title, ' ', description,' ', content)) STORED ,
 author VARCHAR(255) ,
 category VARCHAR(255) ,
 FULLTEXT INDEX full_text_index (fulltext_col) 
) COLLATE='utf8_bin' ENGINE=InnoDB;
```

**Step2.Show description of the table**

The description of your table is like below.

```
mysql> desc article;
+--------------+--------------+------+-----+---------------------+-----------------------------+
| Field        | Type         | Null | Key | Default             | Extra                       |
+--------------+--------------+------+-----+---------------------+-----------------------------+
| entry_id     | int(11)      | NO   | PRI | NULL                |                             |
| site_id      | int(11)      | NO   |     | NULL                |                             |
| published_at | timestamp    | NO   |     | CURRENT_TIMESTAMP   | on update CURRENT_TIMESTAMP |
| updated_at   | timestamp    | NO   |     | 0000-00-00 00:00:00 |                             |
| title        | varchar(255) | YES  |     | NULL                |                             |
| link         | text         | YES  |     | NULL                |                             |
| description  | text         | YES  |     | NULL                |                             |
| content      | text         | YES  |     | NULL                |                             |
| fulltext_col | text         | YES  | MUL | NULL                | STORED GENERATED            |
| author       | varchar(255) | YES  |     | NULL                |                             |
| category     | varchar(255) | YES  |     | NULL                |                             |
+--------------+--------------+------+-----+---------------------+-----------------------------+

```

**Step3.Generate model classes**

Write like below to get a java source code(model class) for that table.

```

import org.riversun.d6.predev.D6JavaModelGen4MySQL;

public class Main {

	public static void main(String[] args) {

		D6JavaModelGen4MySQL generator = new D6JavaModelGen4MySQL();

		String text=
				"mysql> desc article;"+"\n"+
				"+--------------+--------------+------+-----+---------------------+-----------------------------+"+"\n"+
				"| Field        | Type         | Null | Key | Default             | Extra                       |"+"\n"+
				"+--------------+--------------+------+-----+---------------------+-----------------------------+"+"\n"+
				"| entry_id     | int(11)      | NO   | PRI | NULL                |                             |"+"\n"+
				"| site_id      | int(11)      | NO   |     | NULL                |                             |"+"\n"+
				"| published_at | timestamp    | NO   |     | CURRENT_TIMESTAMP   | on update CURRENT_TIMESTAMP |"+"\n"+
				"| updated_at   | timestamp    | NO   |     | 0000-00-00 00:00:00 |                             |"+"\n"+
				"| title        | varchar(255) | YES  |     | NULL                |                             |"+"\n"+
				"| link         | text         | YES  |     | NULL                |                             |"+"\n"+
				"| description  | text         | YES  |     | NULL                |                             |"+"\n"+
				"| content      | text         | YES  |     | NULL                |                             |"+"\n"+
				"| fulltext_col | text         | YES  | MUL | NULL                | STORED GENERATED            |"+"\n"+
				"| author       | varchar(255) | YES  |     | NULL                |                             |"+"\n"+
				"| category     | varchar(255) | YES  |     | NULL                |                             |"+"\n"+
				"+--------------+--------------+------+-----+---------------------+-----------------------------+"+"\n";

		String modelClasses = generator.generateModelClass(text);
		System.out.println(modelClasses);
	}

}
```

**Step4.Run to generate source code**

Now, you can get the source code like this.

```
import org.riversun.d6.D6Model;
import org.riversun.d6.annotation.DBColumn;
import org.riversun.d6.annotation.DBTable;

@DBTable(tableName = "article")
public class Article implements D6Model {
	@DBColumn(columnName = "entry_id", columnType = "int(11)", isNullable = false, isPrimaryKey = true)
	public Integer entryId;

	@DBColumn(columnName = "site_id", columnType = "int(11)", isNullable = false)
	public Integer siteId;

	@DBColumn(columnName = "published_at", columnType = "timestamp", isNullable = false)
	public java.sql.Time publishedAt;

	@DBColumn(columnName = "updated_at", columnType = "timestamp", isNullable = false)
	public java.sql.Time updatedAt;

	@DBColumn(columnName = "title", columnType = "varchar(255)")
	public String title;

	@DBColumn(columnName = "link", columnType = "text")
	public String link;

	@DBColumn(columnName = "description", columnType = "text")
	public String description;

	@DBColumn(columnName = "content", columnType = "text")
	public String content;

	@DBColumn(columnName = "fulltext_col", columnType = "text")
	public String fulltextCol;

	@DBColumn(columnName = "author", columnType = "varchar(255)")
	public String author;

	@DBColumn(columnName = "category", columnType = "varchar(255)")
	public String category;

}
```


# Details
See javadoc as follows.

https://riversun.github.io/javadoc/d6/



# Downloads
## maven
- Add dependencies to maven pom.xml file.
```xml
<dependency>
  <groupId>org.riversun</groupId>
  <artifactId>d6</artifactId>
  <version>0.6.2</version>
</dependency>
```

