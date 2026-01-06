/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.util.*;

public class ExcelExecutor {

    public static ResultSet select(Workbook wb, String sql) {

        sql = SQLNormalizer.normalize(sql);
        Map<String, String> parsed = SQLParser.parseSelect(sql);

        Sheet sheet = wb.getSheet(parsed.get("sheet"));
        Map<String, Integer> header = headerIndex(sheet);

        List<Condition> conditions = parsed.containsKey("where")
                ? WhereParser.parse(parsed.get("where"))
                : List.of();

        List<Map<String, String>> rows = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            boolean match = true;

            for (Condition c : conditions) {
                match &= ConditionEvaluator.evaluate(
                        r.getCell(header.get(c.column)), c);
            }

            if (match) {
                Map<String, String> row = new LinkedHashMap<>();
                if (parsed.get("columns").equals("*")) {
                    for (String col : header.keySet()) {
                        row.put(col, r.getCell(header.get(col)).toString());
                    }
                } else {
                    for (String col : parsed.get("columns").split(",")) {
                        row.put(col.trim(),
                                r.getCell(header.get(col.trim())).toString());
                    }
                }
                rows.add(row);
            }
        }
        return new ResultSet(rows);
    }

    public static void update(Workbook wb, String path, String sql) throws Exception {

        sql = SQLNormalizer.normalize(sql);

        if (sql.startsWith("DELETE")) {
            delete(wb, path, sql);
            return;
        }

        if (sql.startsWith("UPDATE")) {

            String sheet = sql.split(" ")[1];
            String set = sql.substring(sql.indexOf("SET") + 3, sql.indexOf("WHERE")).trim();
            String where = sql.substring(sql.indexOf("WHERE") + 5);

            String[] s = set.split("=");
            String[] w = where.split("=");

            Sheet sh = wb.getSheet(sheet);
            Map<String, Integer> header = headerIndex(sh);

            for (int i = 1; i <= sh.getLastRowNum(); i++) {
                Row r = sh.getRow(i);
                if (r.getCell(header.get(w[0].trim())).toString()
                        .equals(w[1].replace("'", "").trim())) {

                    r.getCell(header.get(s[0].trim()))
                            .setCellValue(s[1].replace("'", "").trim());
                }
            }
            save(wb, path);
        }
    }

    private static void delete(Workbook wb, String path, String sql) throws Exception {

        String sheet = sql.split("FROM")[1].split("WHERE")[0].trim();
        String where = sql.split("WHERE")[1];

        Sheet sh = wb.getSheet(sheet);
        Map<String, Integer> header = headerIndex(sh);
        List<Condition> conditions = WhereParser.parse(where);

        for (int i = sh.getLastRowNum(); i >= 1; i--) {
            Row r = sh.getRow(i);
            boolean match = true;

            for (Condition c : conditions) {
                match &= ConditionEvaluator.evaluate(
                        r.getCell(header.get(c.column)), c);
            }
            if (match) sh.removeRow(r);
        }
        save(wb, path);
    }

    private static Map<String, Integer> headerIndex(Sheet sheet) {
        Map<String, Integer> map = new LinkedHashMap<>();
        Row header = sheet.getRow(0);
        for (Cell c : header) {
            map.put(c.getStringCellValue(), c.getColumnIndex());
        }
        return map;
    }

    private static void save(Workbook wb, String path) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            wb.write(fos);
        }
    }
}
