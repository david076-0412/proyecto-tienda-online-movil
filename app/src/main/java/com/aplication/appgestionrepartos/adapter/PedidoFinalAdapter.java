package com.aplication.appgestionrepartos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.administrador.PedidoEditarActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PedidoFinalAdapter extends FirestoreRecyclerAdapter<Pedido, PedidoFinalAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    private PedidoFinalAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;



    public PedidoFinalAdapter(@NonNull FirestoreRecyclerOptions<Pedido> options, Activity activity, FragmentManager fm){
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull PedidoFinalAdapter.ViewHolder viewHolder, int i, @NonNull Pedido Pedido) {
        DecimalFormat format = new DecimalFormat("0.00");
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        final String id = documentSnapshot.getId();

        final String codigoidd = documentSnapshot.getString("id_user");


        final String nameCli = Pedido.getNombre();

        viewHolder.name.setText(Pedido.getName());
        viewHolder.nombre.setText(Pedido.getNombre());
        viewHolder.color.setText(Pedido.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Pedido.getVaccine_price()));

        viewHolder.pago.setText(Pedido.getPago());

        viewHolder.hora.setText(Pedido.getHora());





        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          SEND DATA ACTIVITY
                Intent i = new Intent(activity, PedidoEditarActivity.class);
                i.putExtra("id_user", id);
                i.putExtra("name", nameCli);
                i.putExtra("user", codigoidd);
                activity.startActivity(i);

//            SEND DATA FRAGMENT
//            CreatePetFragment createPetFragment = new CreatePetFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("id_pet", id);
//            createPetFragment.setArguments(bundle);
//            createPetFragment.show(fm, "open fragment");
            }
        });


        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(v.getRootView().getContext());
                dialogo2.setTitle("AVISO");
                dialogo2.setMessage("¿Estas seguro que deseas eliminar la compra?");
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo2, int idt) {




                        mFirestore.collection("pedidofinal").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    mFirestore.collection("pedidofinal").document(snapshot.getId()).delete();


                                }
                            }
                        });



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
    public PedidoFinalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pedidofinal_single, parent,false);

        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, nombre, color, vaccine_price, pago,hora;
        ImageView btn_edit, btn_delete;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
            nombre = itemView.findViewById(R.id.nombrecliente);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);
            pago = itemView.findViewById(R.id.pago);
            hora = itemView.findViewById(R.id.hora);


            btn_edit = itemView.findViewById(R.id.btn_editar);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);


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


    public void setOnItemClickListener(PedidoFinalAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;

    }





}
