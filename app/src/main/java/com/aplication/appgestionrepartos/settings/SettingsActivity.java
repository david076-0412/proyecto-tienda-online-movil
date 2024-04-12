package com.aplication.appgestionrepartos.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();






    }





    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }



}