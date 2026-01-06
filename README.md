# Excel SQL Engine

SQL-like query engine for Excel using Apache POI (3.17).

## Features
- SELECT / WHERE / AND / OR
- Numeric & Date comparisons
- Fillo-style ResultSet
- POI 3.17 compatible

## Usage
```java
ExcelDB db = ExcelDB.connect("TestData.xlsx");
ResultSet rs = db.executeQuery("SELECT * FROM Sheet1");
while (rs.next()) {
    System.out.println(rs.getField("Column"));
}
db.close();
```
