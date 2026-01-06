/**
 * Author: Jeevithan Ambikapathy
 * Description: SQL-like Excel database connection using Apache POI
 */
package exceldb;

import org.apache.poi.ss.usermodel.*;
import java.io.*;

public class ExcelDB {
    private Workbook workbook;
    private String path;

    private ExcelDB(String path) throws Exception {
        this.path = path;
        this.workbook = WorkbookFactory.create(new File(path));
    }

    public static ExcelDB connect(String path) throws Exception {
        return new ExcelDB(path);
    }

    public ResultSet executeQuery(String sql) {
        return ExcelExecutor.select(workbook, sql);
    }

    public void executeUpdate(String sql) throws Exception {
        ExcelExecutor.update(workbook, path, sql);
    }

    public void close() throws Exception {
        workbook.close();
    }
}
