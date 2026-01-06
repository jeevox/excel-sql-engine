/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import org.apache.poi.ss.usermodel.*;
import java.util.*;

public class SchemaValidator {

    public static void validate(Sheet sheet, List<String> requiredColumns) {

        Set<String> headers = new HashSet<>();
        for (Cell c : sheet.getRow(0)) {
            headers.add(c.getStringCellValue());
        }

        for (String col : requiredColumns) {
            if (!headers.contains(col)) {
                throw new RuntimeException("Missing column: " + col);
            }
        }
    }
}