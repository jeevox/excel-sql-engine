/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import java.util.*;

public class ResultSet {

    private List<Map<String, String>> rows;
    private int cursor = -1;

    ResultSet(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public boolean next() {
        cursor++;
        return cursor < rows.size();
    }

    public String getString(String column) {
        return rows.get(cursor).get(column);
    }

    public Set<String> getFieldNames() {
        return rows.isEmpty() ? Set.of() : rows.get(0).keySet();
    }

    public int getCount() {
    return rows == null ? 0 : rows.size();
    }

    public String getField(String columnName) {
    if (cursor < 0 || cursor >= rows.size()) {
        throw new IllegalStateException(
            "Cursor not positioned on a valid row. Call next() first.");
    }
    return rows.get(cursor).get(columnName);
    }
}
