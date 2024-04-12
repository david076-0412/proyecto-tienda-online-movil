package com.aplication.appgestionrepartos.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aplication.appgestionrepartos.administrador.ListaProveedoresFragment;
import com.aplication.appgestionrepartos.administrador.RegistroProveedoresFragment;

public class fragmentmanager extends FragmentPagerAdapter {


    private int tabno;

    public fragmentmanager(@NonNull FragmentManager fm, int behavior, int tabno){
        super(fm,behavior);
        this.tabno = tabno;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){

            case 0 :



                fragment = new RegistroProveedoresFragment();

                break;



            case 1 :


                fragment = new ListaProveedoresFragment();

                break;




        }

        return fragment;







    }


    @Override
    public int getCount() {
        return tabno;
    }
}
