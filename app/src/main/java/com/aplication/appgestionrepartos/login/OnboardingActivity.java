package com.aplication.appgestionrepartos.login;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aplication.appgestionrepartos.BuildConfig;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ViewPagerAdapter;
import com.aplication.appgestionrepartos.chat.LoginActivity;

public class OnboardingActivity extends AppCompatActivity {


    int REQUEST_CODE = 200;


    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;



    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    Thread splashTread;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);



        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);






        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) > 0){

                    mSLideViewPager.setCurrentItem(getitem(-1),true);

                }

            }
        });



        nextbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                if (getitem(0) < 3)
                    mSLideViewPager.setCurrentItem(getitem(1),true);
                else {



                    primervezrun();




                }

            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {


                primervezrun();






            }
        });

        mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSLideViewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);



        verificarPermisos();




    }









    //Retorna: 0 primera vez / 1 no es primera vez / 2 nueva versión
    public static int getFirstTimeRun(Context contexto) {
        SharedPreferences sp = contexto.getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }



    @SuppressLint("RestrictedApi")
    public void primervezrun(){

        switch(getFirstTimeRun(OnboardingActivity.this)) {

            case 0:


                Log.d(TAG, "Es la primera vez!");
                // acá haces el intent a tu activity especial

                Intent i = new Intent(getApplicationContext(), OnboardingActivity.class);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(i);
                finish();




                break;

            case 1:


                Intent in = new Intent(OnboardingActivity.this, LoginActivity.class);

                overridePendingTransition(R.anim.left_in, R.anim.left_out);

                startActivity(in);
                finish();




                Log.d(TAG, "Ya has iniciado la app alguna vez");


                //
                break;

            case 2:

                Log.d(TAG, "Es una versión nueva");
                //







                break;


        }





    }







    private void verificarPermisos(){


        int PermisosLocation = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        int PermisosSmS = ContextCompat.checkSelfPermission(OnboardingActivity.this,Manifest.permission.SEND_SMS);

        int PermisosStorageWrite = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int PermisosStorageRead = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        int PermisosPhoneRead = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.READ_PHONE_STATE);


        int PermisosProcessCall = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS);


        int PermisosCamera = ContextCompat.checkSelfPermission(OnboardingActivity.this, Manifest.permission.CAMERA);





        if (PermisosSmS == PackageManager.PERMISSION_GRANTED && PermisosLocation == PackageManager.PERMISSION_GRANTED && PermisosStorageWrite == PackageManager.PERMISSION_GRANTED && PermisosStorageRead == PackageManager.PERMISSION_GRANTED && PermisosPhoneRead == PackageManager.PERMISSION_GRANTED && PermisosProcessCall == PackageManager.PERMISSION_GRANTED && PermisosCamera == PackageManager.PERMISSION_GRANTED){

            //Toast.makeText(OnboardingActivity.this, "Permiso SMS Concedido", Toast.LENGTH_SHORT).show();

            //Toast.makeText(OnboardingActivity.this, "Permiso Location Concedido", Toast.LENGTH_SHORT).show();


            Intent in = new Intent(OnboardingActivity.this, LoginActivity.class);

            overridePendingTransition(R.anim.left_in, R.anim.left_out);

            startActivity(in);
            finish();




        }else{

            requestPermissions(new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.CAMERA}, REQUEST_CODE);

        }


    }





    public void setUpindicator(int position){

        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }




    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                backbtn.setVisibility(View.VISIBLE);

            }else {

                backbtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }






}