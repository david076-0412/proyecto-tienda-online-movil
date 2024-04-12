package com.aplication.appgestionrepartos.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Pet;
//import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class InicioAdapter extends FirestoreRecyclerAdapter<Pet,InicioAdapter.ViewHolder>{

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private OnItemClickListener onItemClickListener;


    public InicioAdapter(@NonNull FirestoreRecyclerOptions<Pet> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull InicioAdapter.ViewHolder viewHolder, int position, @NonNull final Pet Pet) {
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
                        .into(viewHolder.photo_pet);


        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }



    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_inicio_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, color, vaccine_price;
        ImageView photo_pet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
            age = itemView.findViewById(R.id.edad);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);
            photo_pet = itemView.findViewById(R.id.photo);






        }
    }



    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot , int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;

    }






}
