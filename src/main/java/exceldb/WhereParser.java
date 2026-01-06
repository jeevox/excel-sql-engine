/**
 * Author: Jeevithan Ambikapathy
 */
package exceldb;

import java.util.*;

public class WhereParser {

    public static List<Condition> parse(String where) {
        List<Condition> list = new ArrayList<>();

        String[] parts = where.split("(?i) AND | OR ");
        boolean isOr = where.toUpperCase().contains(" OR ");

        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            Condition c = new Condition();
            c.logic = (i == 0) ? Logic.AND : (isOr ? Logic.OR : Logic.AND);

            if (p.contains(">=")) {
                fill(c, p, ">=", Operator.GTE);
            } else if (p.contains("<=")) {
                fill(c, p, "<=", Operator.LTE);
            } else if (p.contains(">")) {
                fill(c, p, ">", Operator.GT);
            } else if (p.contains("<")) {
                fill(c, p, "<", Operator.LT);
            } else {
                fill(c, p, "=", Operator.EQ);
            }
            list.add(c);
        }
        return list;
    }

    private static void fill(Condition c, String exp, String op, Operator operator) {
        String[] a = exp.split(op);
        c.column = a[0].trim();
        c.value = a[1].replace("'", "").trim();
        c.operator = operator;
    }
}