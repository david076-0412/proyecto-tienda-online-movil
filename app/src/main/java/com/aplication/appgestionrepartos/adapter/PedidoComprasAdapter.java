package com.aplication.appgestionrepartos.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.cliente.ListadoProductosActivity;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PedidoComprasAdapter extends FirestoreRecyclerAdapter<Pedido, PedidoComprasAdapter.ViewHolder> {

    private  OnItemClickListener onItemClickListener;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    Activity activity;




    public PedidoComprasAdapter(@NonNull FirestoreRecyclerOptions<Pedido> options,Activity activity){
        super(options);
        this.activity = activity;

    }

    @Override
    protected void onBindViewHolder(@NonNull PedidoComprasAdapter.ViewHolder viewHolder, int i, @NonNull Pedido Pedido) {
        DecimalFormat format = new DecimalFormat("0.00");
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        final String id = documentSnapshot.getId();


        final String codigoidd = documentSnapshot.getString("id_user");

        final String codigo = documentSnapshot.getString("codigo");


        viewHolder.name.setText(Pedido.getName());
        viewHolder.codigo.setText(Pedido.getCodigo());
        viewHolder.color.setText(Pedido.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Pedido.getVaccine_price()));



        viewHolder.btn_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, ListadoProductosActivity.class);

                i.putExtra("codigo",codigo);


                activity.startActivity(i);

            }
        });




        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(view.getRootView().getContext());
                dialogo2.setTitle("AVISO");
                dialogo2.setMessage("¿Estas seguro que deseas cancelar la compra?");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int idt) {


                        mFirestore.collection("comprasfinal/"+codigoidd+"/cliente").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    mFirestore.collection("comprasfinal/"+codigoidd+"/cliente").document(id).delete();


                                }
                            }
                        });








                    }
                });
                dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int id) {

                    }
                });
                dialogo2.show();




















            }
        });




    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pedidocompras_single, parent,false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, codigo, color, vaccine_price;
        ImageView btn_listar, btn_eliminar;




        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            codigo = itemView.findViewById(R.id.codigoidd);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);

            btn_listar = itemView.findViewById(R.id.btn_listar);

            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);





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




