package com.aplication.appgestionrepartos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.repartidor.InfoClienteActivity;
import com.aplication.appgestionrepartos.repartidor.ProductoClienteActivity;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.aplication.appgestionrepartos.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//import com.squareup.picasso.Picasso;


public class RepartidorAdapter extends FirestoreRecyclerAdapter<Usuarios,RepartidorAdapter.ViewHolder>{
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    public RepartidorAdapter(@NonNull FirestoreRecyclerOptions<Usuarios> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull RepartidorAdapter.ViewHolder viewHolder, int i, @NonNull Usuarios usuarios) {

//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        final String codigoid = documentSnapshot.getString("id_user");

        final String apellido = documentSnapshot.getString("apellido");




        viewHolder.name.setText(usuarios.getName() + " "+apellido);
        viewHolder.dni.setText(usuarios.getDni());
        viewHolder.direccion.setText(usuarios.getDireccion());


        String photoPet = usuarios.getPhoto();

        final String nameCli = usuarios.getName();

        try {
            if (!photoPet.equals(""))
                /*
                Picasso.with(activity.getApplicationContext())
                        .load(photoPet)
                        .resize(150, 150)
                        .into(viewHolder.photo_pet);
                 */

                Glide.with(activity.getApplicationContext())
                        .load(photoPet)
                        .circleCrop()
                        .error(R.drawable.usuario)
                        .into(viewHolder.photo_pet);


        } catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }



        viewHolder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          SEND DATA ACTIVITY
                Intent i = new Intent(activity, InfoClienteActivity.class);

                i.putExtra("id_pet", id);
                i.putExtra("name", nameCli);
                i.putExtra("id_user", codigoid);



                activity.startActivity(i);

//            SEND DATA FRAGMENT
//            CreatePetFragment createPetFragment = new CreatePetFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("id_pet", id);
//            createPetFragment.setArguments(bundle);
//            createPetFragment.show(fm, "open fragment");
            }
        });






    }

    @NonNull
    @Override
    public RepartidorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_repartidor_single, parent, false);
        return new ViewHolder(v);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, dni, direccion;
        ImageView btn_info, photo_pet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
            dni = itemView.findViewById(R.id.dni);
            direccion = itemView.findViewById(R.id.direccion);


            photo_pet = itemView.findViewById(R.id.photo);

            btn_info = itemView.findViewById(R.id.btn_info);
        }
    }






}
