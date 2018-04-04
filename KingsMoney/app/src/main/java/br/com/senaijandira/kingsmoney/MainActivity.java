package br.com.senaijandira.kingsmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import br.com.senaijandira.kingsmoney.dao.LancamentoDAO;
import br.com.senaijandira.kingsmoney.util.MoneyFormatter;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout appBar;
    private Window window;
    private TextView txtSaldoGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        appBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        txtSaldoGeral = (TextView) findViewById(R.id.txt_saldo_geral);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        double[] saldoGeral = LancamentoDAO.getInstance().getSaldoGeral(this);
        double saldo = saldoGeral[1] - saldoGeral[0];
        txtSaldoGeral.setText(MoneyFormatter.format(saldo));
        // Muda a cor principal do aplicativo dependendo se o saldo é negativo ou positivo.
        if (saldo < 0) {
            appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryNegative));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDarkNegative));
        } else {
            appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            window.setStatusBarColor((ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }
    }

    public void abrirLancamentos(View view) {
        startActivity(new Intent(getApplicationContext(), LancamentosActivity.class));
    }

    public void abrirEstatisticas(View view) {
        startActivity(new Intent(getApplicationContext(), EstatisticasActivity.class));
    }

    public void abrirCategorias(View view) {
        startActivity(new Intent(getApplicationContext(), CategoriasActivity.class));
    }
}