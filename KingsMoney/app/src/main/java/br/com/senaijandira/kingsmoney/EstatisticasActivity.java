package br.com.senaijandira.kingsmoney;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class EstatisticasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fragmentos, usados para criar duas tabs na tela de estatísticas.
        SectionsPagerAdapter pagerEstatisticas = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager containerEstatisticas = (ViewPager) findViewById(R.id.container_estatisticas);
        containerEstatisticas.setAdapter(pagerEstatisticas);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_estatisticas);
        tabLayout.setupWithViewPager(containerEstatisticas);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DespesasCategoriaFragment();
                case 1:
                    return new DespesasMesFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Categorias";
                case 1:
                    return "Mês";
            }

            return null;
        }
    }
}