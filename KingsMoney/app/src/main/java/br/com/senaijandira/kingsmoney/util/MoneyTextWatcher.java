package br.com.senaijandira.kingsmoney.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;


public class MoneyTextWatcher implements TextWatcher {
    // Classe usada para fazer mascara na digitação do preço.
    private EditText editText;
    private double value;

    public MoneyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        if (!s.equals("")) {
            editText.removeTextChangedListener(this);
            String cleanString = s.replaceAll("[^0-9]", "");
            BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
            String formatted = MoneyFormatter.getFormatter().format(parsed);
            value = parsed.doubleValue();
            editText.setText(formatted);
            editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);
        }
    }
}