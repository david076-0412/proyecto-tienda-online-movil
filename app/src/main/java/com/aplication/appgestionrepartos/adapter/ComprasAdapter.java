package com.aplication.appgestionrepartos.adapter;

import android.app.Activity;
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

import com.aplication.appgestionrepartos.model.Productos;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aplication.appgestionrepartos.R;

import java.text.DecimalFormat;

public class ComprasAdapter  extends FirestoreRecyclerAdapter<Productos,ComprasAdapter.ViewHolder>{
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;


    public ComprasAdapter(@NonNull FirestoreRecyclerOptions<Productos> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Productos Productos) {
        DecimalFormat format = new DecimalFormat("0.00");
//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

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

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePet(id);
                deletePro(id);

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_carrito_single, parent, false);
        return new ViewHolder(v);
    }

    private void deletePet(String id) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        mFirestore.collection("carrito/"+idUser+"/pedido").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePro(String id) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        mFirestore.collection("pedido/"+idUser+"/cliente").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, color, vaccine_price;
        ImageView btn_delete, photo_pet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.Nombre);
            age = itemView.findViewById(R.id.Edad);
            color = itemView.findViewById(R.id.Color);
            vaccine_price = itemView.findViewById(R.id.Precio_Vacuna);

            photo_pet = itemView.findViewById(R.id.foto);

            btn_delete = itemView.findViewById(R.id.btn_eliminar);

        }
    }

}
