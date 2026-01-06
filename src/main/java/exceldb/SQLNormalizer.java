/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

public class SQLNormalizer {

    public static String normalize(String sql) {
        return sql
            .replaceAll("(?i)select", "SELECT")
            .replaceAll("(?i)from", "FROM")
            .replaceAll("(?i)where", "WHERE")
            .replaceAll("(?i)and", "AND")
            .replaceAll("(?i)or", "OR")
            .replaceAll("(?i)insert", "INSERT")
            .replaceAll("(?i)update", "UPDATE")
            .replaceAll("(?i)delete", "DELETE")
            .replaceAll("(?i)set", "SET")
            .replaceAll("(?i)values", "VALUES")
            .trim();
    }
}