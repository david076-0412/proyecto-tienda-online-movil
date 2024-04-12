package com.aplication.appgestionrepartos.administrador_ui.monitoreo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.appgestionrepartos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;


public class MonitoreoFragment extends Fragment{


    CircularProgressBar circularProgressBar1;
    CircularProgressBar circularProgressBar2;
    CircularProgressBar circularProgressBar3;


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


    private PieChart pieChart;

    TextView tvR, tvPython, tvCPP, tvJava;


    Double clientestotal;


    int clientestotalProgress;

    int totalquejas, totalsugerencias;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_monitoreo, container, false);



        circularProgressBar1 = (CircularProgressBar) view.findViewById(R.id.circularProgress);


        circularProgressBar1.setProgressColor(getResources().getColor(R.color.link_text_material_light));
        circularProgressBar1.setProgressWidth(30);
        circularProgressBar1.setTextColor(getResources().getColor(R.color.button_material_dark));

        pressok();




        pieChart = view.findViewById(R.id.piechart);



        tvR = view.findViewById(R.id.tvR);
        tvPython = view.findViewById(R.id.tvPython);
        //tvCPP = view.findViewById(R.id.tvCPP);
        //tvJava = view.findViewById(R.id.tvJava);

        pieChart = view.findViewById(R.id.piechart);



        setData();






        return view;






    }





    public void pressok(){


        mFirestore.collection("acumulador/conteo/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot1 : task.getResult()){

                    Double cantidadclientes = snapshot1.getDouble("codigoS");


                    clientestotal = (cantidadclientes + cantidadclientes);


                    //int newProgress = (int) (Math.random() * 100);


                    clientestotalProgress = (int) ((clientestotal) * 100)/100;



                    if (clientestotalProgress < 100){

                        circularProgressBar1.setProgress(clientestotalProgress);

                    }else if (clientestotalProgress > 100){

                        circularProgressBar1.setProgress(100);



                    }else if (clientestotalProgress == 100){

                        circularProgressBar1.setProgress(100);

                    }













                }


            }
        });




    }









    private void setData()
    {


        mFirestore.collection("Quejas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot3 : task.getResult()){

                    Double cantidad_quejas = snapshot3.getDouble("cantidad_quejas");

                    int cantidadquejastotal = Integer.valueOf(cantidad_quejas.intValue());


                    totalquejas = totalquejas + cantidadquejastotal;



                    // Set the percentage of language used
                    tvR.setText(Integer.toString( totalquejas));





                    mFirestore.collection("Sugerencias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot3 : task.getResult()){

                                Double cantidad_sugerencias = snapshot3.getDouble("cantidad_sugerencias");

                                int cantidadsugerenciastotal = Integer.valueOf(cantidad_sugerencias.intValue());


                                totalsugerencias = totalsugerencias + cantidadsugerenciastotal;



                                if (totalquejas < 100 && totalsugerencias <100){

                                    tvR.setText(Integer.toString(totalquejas));

                                    tvPython.setText(Integer.toString(totalsugerencias));

                                    //tvCPP.setText(Integer.toString(5));
                                    //tvJava.setText(Integer.toString(25));

                                    // Set the data and color to the pie chart
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Quejas",
                                                    Integer.parseInt(tvR.getText().toString()),
                                                    Color.parseColor("#FFA726")));
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Sugerencias",
                                                    Integer.parseInt(tvPython.getText().toString()),
                                                    Color.parseColor("#66BB6A")));


                                /*

                                pieChart.addPieSlice(
                                        new PieModel(
                                                "C++",
                                                Integer.parseInt(tvCPP.getText().toString()),
                                                Color.parseColor("#EF5350")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Java",
                                                Integer.parseInt(tvJava.getText().toString()),
                                                Color.parseColor("#29B6F6")));

                                */


                                    // To animate the pie chart
                                    pieChart.startAnimation();



                                }else if (totalquejas > 100 && totalsugerencias< 100){

                                    tvR.setText(Integer.toString(100));

                                    tvPython.setText(Integer.toString(totalsugerencias));

                                    //tvCPP.setText(Integer.toString(5));
                                    //tvJava.setText(Integer.toString(25));

                                    // Set the data and color to the pie chart
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Quejas",
                                                    Integer.parseInt(tvR.getText().toString()),
                                                    Color.parseColor("#FFA726")));
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Sugerencias",
                                                    Integer.parseInt(tvPython.getText().toString()),
                                                    Color.parseColor("#66BB6A")));


                                /*

                                pieChart.addPieSlice(
                                        new PieModel(
                                                "C++",
                                                Integer.parseInt(tvCPP.getText().toString()),
                                                Color.parseColor("#EF5350")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Java",
                                                Integer.parseInt(tvJava.getText().toString()),
                                                Color.parseColor("#29B6F6")));

                                */


                                    // To animate the pie chart
                                    pieChart.startAnimation();



                                }else if (totalquejas <100 && totalsugerencias > 100){

                                    tvR.setText(Integer.toString(totalquejas));

                                    tvPython.setText(Integer.toString(100));

                                    //tvCPP.setText(Integer.toString(5));
                                    //tvJava.setText(Integer.toString(25));

                                    // Set the data and color to the pie chart
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Quejas",
                                                    Integer.parseInt(tvR.getText().toString()),
                                                    Color.parseColor("#FFA726")));
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Sugerencias",
                                                    Integer.parseInt(tvPython.getText().toString()),
                                                    Color.parseColor("#66BB6A")));


                                /*

                                pieChart.addPieSlice(
                                        new PieModel(
                                                "C++",
                                                Integer.parseInt(tvCPP.getText().toString()),
                                                Color.parseColor("#EF5350")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Java",
                                                Integer.parseInt(tvJava.getText().toString()),
                                                Color.parseColor("#29B6F6")));

                                */


                                    // To animate the pie chart
                                    pieChart.startAnimation();



                                }else if (totalquejas == 100 && totalsugerencias == 100){

                                    tvR.setText(Integer.toString(100));

                                    tvPython.setText(Integer.toString(100));

                                    //tvCPP.setText(Integer.toString(5));
                                    //tvJava.setText(Integer.toString(25));

                                    // Set the data and color to the pie chart
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Quejas",
                                                    Integer.parseInt(tvR.getText().toString()),
                                                    Color.parseColor("#FFA726")));
                                    pieChart.addPieSlice(
                                            new PieModel(
                                                    "Sugerencias",
                                                    Integer.parseInt(tvPython.getText().toString()),
                                                    Color.parseColor("#66BB6A")));


                                /*

                                pieChart.addPieSlice(
                                        new PieModel(
                                                "C++",
                                                Integer.parseInt(tvCPP.getText().toString()),
                                                Color.parseColor("#EF5350")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Java",
                                                Integer.parseInt(tvJava.getText().toString()),
                                                Color.parseColor("#29B6F6")));

                                */


                                    // To animate the pie chart
                                    pieChart.startAnimation();


                                }


















                            }


                        }
                    });


















                }


            }
        });



    }







}