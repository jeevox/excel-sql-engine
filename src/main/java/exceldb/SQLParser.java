/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import java.util.*;

public class SQLParser {

    public static Map<String, String> parseSelect(String sql) {
        Map<String, String> map = new HashMap<>();

        String cols = sql.substring(6, sql.indexOf("FROM")).trim();
        String rest = sql.substring(sql.indexOf("FROM") + 4).trim();

        map.put("columns", cols);

        if (rest.contains("WHERE")) {
            map.put("sheet", rest.substring(0, rest.indexOf("WHERE")).trim());
            map.put("where", rest.substring(rest.indexOf("WHERE") + 5).trim());
        } else {
            map.put("sheet", rest);
        }
        return map;
    }
}