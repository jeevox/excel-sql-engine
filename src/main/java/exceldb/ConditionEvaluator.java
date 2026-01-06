/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import org.apache.poi.ss.usermodel.*;
import java.time.LocalDate;

public class ConditionEvaluator {

    public static boolean evaluate(Cell cell, Condition c) {

        if (cell == null) return false;

        if (cell.getCellTypeEnum() == CellType.NUMERIC && !DateUtil.isCellDateFormatted(cell)) {
            double a = cell.getNumericCellValue();
            double b = Double.parseDouble(c.value);
            return compare(a, b, c.operator);
        }

        if (DateUtil.isCellDateFormatted(cell)) {
            LocalDate a = cell.getDateCellValue().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
            LocalDate b = LocalDate.parse(c.value);
            return compare(a.compareTo(b), 0, c.operator);
        }

        return cell.toString().equals(c.value);
    }

    private static boolean compare(double a, double b, Operator op) {
        switch (op) {
            case GT: return a > b;
            case LT: return a < b;
            case GTE: return a >= b;
            case LTE: return a <= b;
            default: return a == b;
        }
    }
}
