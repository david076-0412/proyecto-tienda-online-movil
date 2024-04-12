package com.aplication.appgestionrepartos.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aplication.appgestionrepartos.administrador.ActualizarProveedorActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Proveedor;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProveedoreAdapter extends FirestoreRecyclerAdapter<Proveedor,ProveedoreAdapter.ViewHolder> {


    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private Context context;

    fragmentmanager viewPagerAdapter;

    ViewPager viewPager;


    public ProveedoreAdapter(@NonNull FirestoreRecyclerOptions<Proveedor> options) {
        super(options);


    }


    @Override
    protected void onBindViewHolder(@NonNull ProveedoreAdapter.ViewHolder viewHolder, int i, @NonNull Proveedor Proveedor) {

//
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();



        viewHolder.nombre.setText(Proveedor.getNombre());

        viewHolder.apellido.setText(Proveedor.getApellido());

        viewHolder.dni.setText(Proveedor.getDni());

        viewHolder.empresa.setText(Proveedor.getEmpresa());

        viewHolder.celular.setText(Proveedor.getCelular());




        String photoPet = Proveedor.getPhoto();



        try {
            if (!photoPet.equals(""))
                /*
                Picasso.with(activity.getApplicationContext())
                        .load(photoPet)
                        .resize(150, 150)
                        .into(viewHolder.photo_pet);
                 */
                Glide.with(viewHolder.photo_pro.getContext())
                        .load(photoPet)
                        .circleCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(viewHolder.photo_pro);


        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }



        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(v.getContext(), ActualizarProveedorActivity.class);

                i.putExtra("id_proveedor", id);

                v.getContext().startActivity(i);




            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirestore.collection("Proveedores").document(id).delete();






            }
        });

    }











    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_proveedor_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, apellido, dni, empresa, celular;

        ImageView btn_edit, btn_delete, photo_pro;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            nombre = itemView.findViewById(R.id.nombre);

            apellido = itemView.findViewById(R.id.apellido);

            dni = itemView.findViewById(R.id.dni);

            empresa = itemView.findViewById(R.id.empresa);

            celular = itemView.findViewById(R.id.celular);



            photo_pro = itemView.findViewById(R.id.photo);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }








}
