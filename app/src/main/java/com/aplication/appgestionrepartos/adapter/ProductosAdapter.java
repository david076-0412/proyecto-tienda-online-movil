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

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Productos;

import java.text.DecimalFormat;

public class ProductosAdapter extends FirestoreRecyclerAdapter<Productos,ProductosAdapter.ViewHolder>{

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    private ProductosAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;



    public ProductosAdapter(@NonNull FirestoreRecyclerOptions<Productos> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductosAdapter.ViewHolder viewHolder, int i, @NonNull Productos Productos) {
        DecimalFormat format = new DecimalFormat("0.00");
//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        String namepet = documentSnapshot.getString("name");


        viewHolder.name.setText(Productos.getName());
        viewHolder.age.setText(format.format(Productos.getAge()));
        viewHolder.color.setText(Productos.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Productos.getVaccine_price()));








        String photoPet = Productos.getPhoto();

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
                        .error(R.drawable.ic_launcher_background)
                        .into(viewHolder.photo_pet);


        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }



    }

    @NonNull
    @Override
    public ProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_carrito_repartidor, parent, false);
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


    public void setOnItemClickListener(ProductosAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;

    }




}

