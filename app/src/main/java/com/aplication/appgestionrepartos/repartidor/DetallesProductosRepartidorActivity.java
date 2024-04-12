package com.aplication.appgestionrepartos.repartidor;

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
import android.widget.Button;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.fragmentmanagerRepartidor;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DetallesProductosRepartidorActivity extends AppCompatActivity {


    TabLayout tabLayout;
    TabItem tab1, tab2, tab3;

    ViewPager viewPager;


    Button btn_exit;


    fragmentmanagerRepartidor fragmentmanagerRepartidor;

    ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;


    String idcliente, codigoidd,nameCli;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_productos_repartidor);


        tabLayout = findViewById(R.id.ctablayout);
        tab1 = findViewById(R.id.ctab1);
        tab2 = findViewById(R.id.ctab2);
        tab3 = findViewById(R.id.ctab3);




        codigoidd = getIntent().getStringExtra("user");

        nameCli = getIntent().getStringExtra("name");

        idcliente = getIntent().getStringExtra("id_user");







        viewPager = (ViewPager) findViewById(R.id.pageholder);

        drawerLayout = findViewById(R.id.mydrawer);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        setViewPager();










    }







    private void setViewPager(){

        fragmentmanagerRepartidor = new fragmentmanagerRepartidor(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(fragmentmanagerRepartidor);




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
        getMenuInflater().inflate(R.menu.activity_repartidor_detalles_drawer, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.volver_inicio:


                Intent i = new Intent(DetallesProductosRepartidorActivity.this,ProductoClienteActivity.class);
                i.putExtra("id_user", idcliente);




                startActivity(i);




                return true;


            case R.id.refresh:


                Intent intent = new Intent(getApplicationContext(), DetallesProductosRepartidorActivity.class);

                intent.putExtra("id_user",idcliente);

                intent.putExtra("name",nameCli);

                intent.putExtra("user",codigoidd);




                startActivity(intent);



                return true;


        }


        return super.onOptionsItemSelected(item);
    }






}
