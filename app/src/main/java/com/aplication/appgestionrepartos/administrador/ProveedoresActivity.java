package com.aplication.appgestionrepartos.administrador;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.fragmentmanager;
import com.aplication.appgestionrepartos.interfaz.InterfazAdministradorActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProveedoresActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tab1, tab2;

    ViewPager viewPager;

    fragmentmanager fragmentmanager;

    ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);


        tabLayout = findViewById(R.id.ctablayout);
        tab1 = findViewById(R.id.ctab1);
        tab2 = findViewById(R.id.ctab2);

        viewPager = (ViewPager) findViewById(R.id.pageholder);

        drawerLayout = findViewById(R.id.mydrawer);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        setViewPager();




    }



    private void setViewPager(){

        fragmentmanager = new fragmentmanager(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(fragmentmanager);




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_proveedores_drawer, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:

                startActivity(new Intent(ProveedoresActivity.this, InterfazAdministradorActivity.class));
                finish();


                return true;


        }


        return super.onOptionsItemSelected(item);
    }





}