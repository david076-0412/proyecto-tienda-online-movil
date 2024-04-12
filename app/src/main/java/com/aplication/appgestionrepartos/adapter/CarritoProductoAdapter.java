package com.aplication.appgestionrepartos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Productos;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CarritoProductoAdapter extends FirestoreRecyclerAdapter<Productos,CarritoProductoAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


    public CarritoProductoAdapter(@NonNull FirestoreRecyclerOptions<Productos> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Productos Productos) {


        DecimalFormat format = new DecimalFormat("0.00");
//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();


        final String namePro=documentSnapshot.getString("name");


        viewHolder.name.setText(Productos.getName());
        viewHolder.age.setText(format.format(Productos.getAge()));
        viewHolder.color.setText(Productos.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Productos.getVaccine_price()));


        String photoPet = Productos.getPhoto();

        try {
            if (!photoPet.equals(""))


                Glide.with(viewHolder.photo_pet.getContext())
                        .load(photoPet)
                        .circleCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(viewHolder.photo_pet);



        } catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }







        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updaterestablecer(id);

                deletePet(id,v);
                deletePro(id,v,namePro);

                deleteConteoProductos(id, v);

            }
        });


    }

    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_carrito_single, parent, false);
        return new ViewHolder(v);
    }



    private void updaterestablecer(String id){


        mFirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                Map<String, Object> mapCA = new HashMap<>();


                //-----------------------------------------------------------

                String cantidadproducto = documentSnapshot.getString("age");

                double cantidadPro = documentSnapshot.getDouble("cantidadPro");

                int cantidadProrr = (int) cantidadPro;

                double cantidadR = documentSnapshot.getDouble("cantidadR");

                int cantidadRp = (int) cantidadR;

                int cantidadrestablecer = cantidadProrr + cantidadRp;

                String cantidadrestableceryy = String.valueOf(cantidadrestablecer);


                mapCA.put("age",cantidadrestableceryy);

                //-----------------------------------------------------------

                mFirestore.collection("Compraseditado").document(id).update(mapCA);



            }
        });








    }





    private void deletePet(String id, View view) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        mFirestore.collection("carrito/" + idUser + "/pedido").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(view.getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deletePro(String id, View view,String namePro) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();
        mFirestore.collection("pedido/" + idUser + "/cliente").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //Toast.makeText(view.getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }

        });

        HashMap<String, Object> mapA = new HashMap<>();
        mapA.put("producto",0);



        mFirestore.collection("acumulador/" + idUser + "/Cliente").document(namePro).update(mapA);

        mFirestore.collection("acumulador/" + idUser + "/Cliente").document("pedido").update(mapA);



    }


    private void deleteConteoProductos(String id, View view) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();


        mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {


                double cantidadProductostotal = documentSnapshot2.getDouble("cont_productos");


                int cantidadProductostotalww = (int) cantidadProductostotal;


                if (cantidadProductostotalww == 0){

                    int cantidadProductostotaltt = 0;

                    Map<String, Object> mapCP = new HashMap<>();




                    mapCP.put("cont_productos",cantidadProductostotaltt);


                    mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapCP);





                }else if (cantidadProductostotalww >=1){

                    int cantidadProductostotaltt = (int) cantidadProductostotal-1;

                    Map<String, Object> mapCP = new HashMap<>();




                    mapCP.put("cont_productos",cantidadProductostotaltt);


                    mFirestore.collection("acumulador/"+idUser+"/Cliente").document("pedido").update(mapCP);




                }












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

