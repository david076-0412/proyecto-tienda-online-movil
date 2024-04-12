package com.aplication.appgestionrepartos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.administrador_ui.pagos.DetallesClienteActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PagosAdministradorAdapter extends FirestoreRecyclerAdapter<Usuarios,PagosAdministradorAdapter.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;


    private PagosAdministradorAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;



    public PagosAdministradorAdapter(@NonNull FirestoreRecyclerOptions<Usuarios> options,Activity activity, FragmentManager fm) {
        super(options);

        this.activity = activity;
        this.fm = fm;


    }



    @Override
    protected void onBindViewHolder(@NonNull PagosAdministradorAdapter.ViewHolder viewHolder, int i, @NonNull Usuarios usuarios) {

//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.name.setText(usuarios.getName() + " " + usuarios.getApellido());

        String photoPet = usuarios.getPhoto();


        try {
            if (!photoPet.equals(""))
                /*
                Picasso.with(activity.getApplicationContext())
                        .load(photoPet)
                        .resize(150, 150)
                        .into(viewHolder.photo_pet);
                 */

                Glide.with(viewHolder.photo_pet.getContext())
                        .load(photoPet)
                        .circleCrop()
                        .error(R.drawable.usuario)
                        .into(viewHolder.photo_pet);


        } catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }




        viewHolder.btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                mFirestore.collection("Administrador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){






                            mFirestore.collection("Administrador").document(snapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {



                                    String idadministrador = documentSnapshot.getString("id");


                                    String nombreadministrador = documentSnapshot.getString("name");
                                    String apellidoadministrador = documentSnapshot.getString("apellido");

                                    String celularadministrador = documentSnapshot.getString("celular");

                                    String tarjetacreditoadministrador = documentSnapshot.getString("tarjeta_credito");

                                    String photoadministrador = documentSnapshot.getString("photo");



                                    //-------------------------------------------------------



                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id_administrador", idadministrador);
                                    map.put("administrador_nombre", nombreadministrador);
                                    map.put("administrador_apellido",apellidoadministrador);
                                    map.put("celular_administrador",celularadministrador);
                                    map.put("tarjeta_credito_administrador",tarjetacreditoadministrador);
                                    map.put("photo_administrador",photoadministrador);




                                    mFirestore.collection("Cliente").document(id).update(map);
                                    mFirestore.collection("Usuario/Cliente/"+id).document("registro").update(map);


                                    Toast.makeText(activity, "Cliente Seleccionado con exito", Toast.LENGTH_SHORT).show();









                                }
                            });








                        }
                    }
                });








            }
        });




        viewHolder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(view.getContext(), DetallesClienteActivity.class);

                i.putExtra("id_pago_cliente", id);

                view.getContext().startActivity(i);





            }
        });






    }




    @NonNull
    @Override
    public PagosAdministradorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_administrador_pagos_single, parent, false);
        return new ViewHolder(v);
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo_pet, btn_agregar, btn_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);

            photo_pet = itemView.findViewById(R.id.photo);

            btn_agregar = itemView.findViewById(R.id.btn_agregar);

            btn_info = itemView.findViewById(R.id.btn_info);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null){
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(position), position);



                    }
                }
            });





        }
    }


    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot , int position);
    }


    public void setOnItemClickListener(PagosAdministradorAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;

    }








}
