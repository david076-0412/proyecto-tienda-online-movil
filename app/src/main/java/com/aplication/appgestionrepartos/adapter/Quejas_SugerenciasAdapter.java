package com.aplication.appgestionrepartos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aplication.appgestionrepartos.administrador.EditorQuejasSugerenciasActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.administrador.InfoQuejas_SugerenciasActivity;
import com.aplication.appgestionrepartos.model.Quejas_Sugerencias;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;


public class Quejas_SugerenciasAdapter extends FirestoreRecyclerAdapter<Quejas_Sugerencias, Quejas_SugerenciasAdapter.ViewHolder>{

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;



    public Quejas_SugerenciasAdapter(@NonNull FirestoreRecyclerOptions<Quejas_Sugerencias> options, Activity activity, FragmentManager fm){
        super(options);
        this.activity = activity;
        this.fm = fm;
    }


    @Override
    protected void onBindViewHolder(@NonNull Quejas_SugerenciasAdapter.ViewHolder viewHolder, int i, @NonNull Quejas_Sugerencias QS) {
        DecimalFormat format = new DecimalFormat("0.00");
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        final String id = documentSnapshot.getId();

        //final String codigoidd = documentSnapshot.getString("id_user");


        viewHolder.nombre.setText(QS.getNombre());
        viewHolder.departamento.setText(QS.getDepartamento());
        viewHolder.incidencia.setText(QS.getIncidencia());
        viewHolder.celular.setText(QS.getCelular());

        viewHolder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, InfoQuejas_SugerenciasActivity.class);
                i.putExtra("id_qs", id);
                activity.startActivity(i);

            }
        });




        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EditorQuejasSugerenciasActivity.class);
                i.putExtra("id_user", id);
                activity.startActivity(i);
            }
        });





        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                mFirestore.collection("Incidencias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            mFirestore.collection("Incidencias").document(id).delete();


                        }
                    }
                });






            }
        });





    }




    @NonNull
    @Override
    public Quejas_SugerenciasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_quejas_sugerencias_single, parent,false);

        return new ViewHolder(v);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, departamento, incidencia, celular;
        ImageView btn_info, btn_edit, btn_delete;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);

            departamento = itemView.findViewById(R.id.departamento);

            incidencia = itemView.findViewById(R.id.incidencia);
            celular = itemView.findViewById(R.id.celular);



            btn_info = itemView.findViewById(R.id.btn_info);
            btn_edit = itemView.findViewById(R.id.btn_editar);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);


        }
    }




}
