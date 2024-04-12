package com.aplication.appgestionrepartos.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aplication.appgestionrepartos.repartidor.ArticulosDetallesFragment;
import com.aplication.appgestionrepartos.repartidor.FirmaDetallesFragment;
import com.aplication.appgestionrepartos.repartidor.GeneralDetallesFragment;

public class fragmentmanagerRepartidor extends FragmentPagerAdapter {

    private int tabno;

    public fragmentmanagerRepartidor(@NonNull FragmentManager fm, int behavior, int tabno){
        super(fm,behavior);
        this.tabno = tabno;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){





            case 0 :



                fragment = new GeneralDetallesFragment();

                break;



            case 1 :


                fragment = new ArticulosDetallesFragment();

                break;



            case 2:

                fragment = new FirmaDetallesFragment();

                break;




        }

        return fragment;







    }


    @Override
    public int getCount() {
        return tabno;
    }


}
