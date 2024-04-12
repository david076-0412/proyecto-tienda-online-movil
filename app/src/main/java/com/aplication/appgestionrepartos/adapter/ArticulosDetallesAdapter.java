package com.aplication.appgestionrepartos.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Productos;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class ArticulosDetallesAdapter extends FirestoreRecyclerAdapter<Productos,ArticulosDetallesAdapter.ViewHolder>{

    private Context context;


    double precio_finalpet, precio_finalpt;


    FragmentManager fm;



    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();







    public ArticulosDetallesAdapter(@NonNull FirestoreRecyclerOptions<Productos> options,FragmentManager fm) {
        super(options);
        this.fm = fm;

    }


    @Override
    protected void onBindViewHolder(@NonNull ArticulosDetallesAdapter.ViewHolder viewHolder, int i, @NonNull Productos Productos) {


        DecimalFormat format = new DecimalFormat("0.00");
//      format.setMaximumFractionDigits(2);
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        Double age = documentSnapshot.getDouble("age");

        int agept = Integer.valueOf(age.intValue());

        String id_user = documentSnapshot.getString("id_user");


        String codigo = documentSnapshot.getString("codigo");


        viewHolder.name.setText(Productos.getName());
        viewHolder.age.setText(String.valueOf(Productos.getAge()));
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

        viewHolder.btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mFirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String cantidad = documentSnapshot.getString("age");

                        int cantidadpt = Integer.parseInt(cantidad);


                        Double precio = documentSnapshot.getDouble("vaccine_price");



                        if (Productos.getAge() !=cantidadpt){

                            Productos.addOne();


                            ArticulosDetallesAdapter.this.notifyDataSetChanged();


                            int cant = Productos.getAge();


                            double precio_final = cant * precio;



                            Map<String, Object> map = new HashMap<>();


                            map.put("age", cant);
                            map.put("vaccine_price", precio_final);


                            mFirestore.collection("compras/"+id_user+"/"+codigo).document(id).update(map);




















                        }
















                    }
                });













            }
        });


        viewHolder.btn_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mFirestore.collection("Compraseditado").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String cantidad = documentSnapshot.getString("age");

                        int cantidadpt = Integer.parseInt(cantidad);


                        Double precio = documentSnapshot.getDouble("vaccine_price");







                        if (Productos.getAge() != 1){

                            Productos.removeOne();

                            ArticulosDetallesAdapter.this.notifyDataSetChanged();


                            int cant = Productos.getAge();

                            double precio_final = cant * precio;



                            Map<String, Object> map = new HashMap<>();



                            map.put("age", cant);
                            map.put("vaccine_price", precio_final);



                            mFirestore.collection("compras/"+id_user+"/"+codigo).document(id).update(map);















                        }







                    }
                });










            }
        });






    }




    @NonNull
    @Override
    public ArticulosDetallesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_productos_repartidor, parent, false);
        return new ViewHolder(v);
    }



    /*


    private void update(String id){


        mFirestore.collection("Compras").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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


*/












    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, color, vaccine_price;



        ImageView btn_mas, btn_menos, photo_pet;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.Nombre);
            age = itemView.findViewById(R.id.Edad);
            color = itemView.findViewById(R.id.Color);
            vaccine_price = itemView.findViewById(R.id.Precio_Vacuna);

            photo_pet = itemView.findViewById(R.id.foto);

            btn_mas = itemView.findViewById(R.id.btn_mas);

            btn_menos = itemView.findViewById(R.id.btn_menos);



        }


    }




}
