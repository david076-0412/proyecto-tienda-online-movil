package com.aplication.appgestionrepartos.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.model.Usuarios;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aplication.appgestionrepartos.R;
//import com.squareup.picasso.Picasso;


public class ContactosAdapter extends FirestoreRecyclerAdapter<Usuarios,ContactosAdapter.ViewHolder>{

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    private ContactosAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;

    public ContactosAdapter(@NonNull FirestoreRecyclerOptions<Usuarios> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactosAdapter.ViewHolder viewHolder, int i, @NonNull Usuarios usuarios) {

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

                Glide.with(activity.getApplicationContext())
                        .load(photoPet)
                        .circleCrop()
                        .error(R.drawable.usuario)
                        .into(viewHolder.photo_pet);


        } catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }








    }


    @NonNull
    @Override
    public ContactosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contactos_p, parent, false);
        return new ViewHolder(v);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo_pet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);

            photo_pet = itemView.findViewById(R.id.photo);


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
