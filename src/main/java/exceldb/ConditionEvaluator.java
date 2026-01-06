/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import org.apache.poi.ss.usermodel.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
 
public class ConditionEvaluator {
 
    public static boolean evaluate(Cell cell, Condition c) {
 
        if (cell == null) return false;
 
        CellType type = cell.getCellTypeEnum();
 
        // ----------------------------
        // NUMERIC (number or date)
        // ----------------------------
        if (type == CellType.NUMERIC) {
 
            // Date stored as numeric
            if (DateUtil.isCellDateFormatted(cell)) {
 
                Date date = cell.getDateCellValue();
                LocalDate actual =
                        date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
 
                LocalDate expected = LocalDate.parse(c.value); // yyyy-MM-dd
                return compare(actual.compareTo(expected), c.operator);
            }
 
            // Pure number
            double actual = cell.getNumericCellValue();
            double expected = Double.parseDouble(c.value);
            return compare(actual, expected, c.operator);
        }
 
        // ----------------------------
        // STRING (text / date as text)
        // ----------------------------
        if (type == CellType.STRING) {
 
            String actual = cell.getStringCellValue().trim();
 
            // Try date comparison if value looks like a date
            if (c.value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate actualDate = LocalDate.parse(actual);
                LocalDate expectedDate = LocalDate.parse(c.value);
                return compare(actualDate.compareTo(expectedDate), c.operator);
            }
 
            // Normal string comparison
            return actual.equals(c.value);
        }
 
        // ----------------------------
        // BOOLEAN
        // ----------------------------
        if (type == CellType.BOOLEAN) {
            return Boolean.parseBoolean(c.value)
                    == cell.getBooleanCellValue();
        }
 
        return false;
    }
 
    // ----------------------------
    // Comparison helpers
    // ----------------------------
    private static boolean compare(double a, double b, Operator op) {
        switch (op) {
            case GT:  return a > b;
            case LT:  return a < b;
            case GTE: return a >= b;
            case LTE: return a <= b;
            case EQ:  return a == b;
            default:  return false;
        }
    }
 
    private static boolean compare(int diff, Operator op) {
        switch (op) {
            case GT:  return diff > 0;
            case LT:  return diff < 0;
            case GTE: return diff >= 0;
            case LTE: return diff <= 0;
            case EQ:  return diff == 0;
            default:  return false;
        }
    }
}
