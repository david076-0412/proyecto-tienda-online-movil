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
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.cliente.AgregarCompraActivity;
import com.aplication.appgestionrepartos.cliente.DetalleActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Pet;

//import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ClienteProductoAdapter extends FirestoreRecyclerAdapter<Pet,ClienteProductoAdapter.ViewHolder>{

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ClienteProductoAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;

    Activity activity;


    public ClienteProductoAdapter(@NonNull FirestoreRecyclerOptions<Pet> options, Activity activity) {
        super(options);
        this.activity = activity;

    }




    @Override
    protected void onBindViewHolder(@NonNull ClienteProductoAdapter.ViewHolder viewHolder, int position, @NonNull final Pet Pet) {
        DecimalFormat format = new DecimalFormat("0.00");
//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.name.setText(Pet.getName());
        viewHolder.age.setText(Pet.getAge());
        viewHolder.color.setText(Pet.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Pet.getVaccine_price()));
        String photoPet = Pet.getPhoto();
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
                        .error(R.drawable.ic_launcher_background)
                        .into(viewHolder.photo_pet);


        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }


        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, AgregarCompraActivity.class);
                i.putExtra("id_pet", id);
                activity.startActivity(i);



            }
        });




        viewHolder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, DetalleActivity.class);
                i.putExtra("id_pet", id);
                activity.startActivity(i);


            }
        });








    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cliente_single, parent, false);
        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, color, vaccine_price;
        ImageView btn_add, btn_info, photo_pet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
            age = itemView.findViewById(R.id.edad);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);
            photo_pet = itemView.findViewById(R.id.photo);
            btn_add = itemView.findViewById(R.id.btn_agregar);
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


    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;

    }














}
