package br.com.senaijandira.kingsmoney.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatter {
    // Classe utilitária para formatar dinheiro no formato "R$ 0,00"
    private static NumberFormat formatter;

    public static NumberFormat getFormatter() {
        if (formatter == null) {
            formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setCurrencySymbol("R$ ");
            dfs.setGroupingSeparator('.');
            dfs.setMonetaryDecimalSeparator(',');
            ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        }

        return formatter;
    }

    public static String format(double number) {
        return getFormatter().format(number);
    }
}