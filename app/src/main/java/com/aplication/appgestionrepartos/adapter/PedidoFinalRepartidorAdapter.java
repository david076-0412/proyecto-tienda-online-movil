package com.aplication.appgestionrepartos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.repartidor.DetallesProductosRepartidorActivity;
import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;


public class PedidoFinalRepartidorAdapter extends FirestoreRecyclerAdapter<Pedido, PedidoFinalRepartidorAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    private PedidoFinalRepartidorAdapter.OnItemClickListener onItemClickListener;
    private int focusedItem = 0;


    boolean oneChecked = false;

    public PedidoFinalRepartidorAdapter(@NonNull FirestoreRecyclerOptions<Pedido> options, Activity activity, FragmentManager fm){
        super(options);

        this.activity = activity;
        this.fm = fm;




    }

    @Override
    protected void onBindViewHolder(@NonNull PedidoFinalRepartidorAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i, @NonNull Pedido Pedido) {
        DecimalFormat format = new DecimalFormat("0.00");
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        final String id = documentSnapshot.getId();

        final String codigoidd = documentSnapshot.getString("id_user");

        final String apellido = documentSnapshot.getString("apellido");

        final String nombreClie= documentSnapshot.getString("name");

        final String nameCli = Pedido.getNombre();





        viewHolder.name.setText(Pedido.getName());
        viewHolder.nombre.setText(Pedido.getNombre()+" "+apellido);
        viewHolder.color.setText(Pedido.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Pedido.getVaccine_price()));

        viewHolder.pago.setText(Pedido.getPago());

        viewHolder.hora.setText(Pedido.getHora());





        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          SEND DATA ACTIVITY
                Intent i = new Intent(activity, DetallesProductosRepartidorActivity.class);
                i.putExtra("id_user", codigoidd);
                i.putExtra("name", nameCli);
                i.putExtra("user", id);


                activity.startActivity(i);


            }
        });





        viewHolder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (viewHolder.check_box.isChecked() == true){

                    //Toast.makeText(activity, ""+id, Toast.LENGTH_SHORT).show();


                    oneChecked = true;



                    HashMap<String, Object> mapC = new HashMap<>();

                    mapC.put("color", "Entregado");

                    mapC.put("pago", "Pagado");




                    //PedidoFinalRepartidorAdapter.this.notifyDataSetChanged();


                    mFirestore.collection("comprasfinal/"+codigoidd+"/cliente").document(id).update(mapC);



                    HashMap<String, Object> mapPP = new HashMap<>();


                    mapPP.put("precio_producto", 0.00);



                    mFirestore.collection("comprasfinal/"+codigoidd+"/MensajePago").document(id).update(mapPP);







                }else if (viewHolder.check_box.isChecked() == false){

                    //Toast.makeText(activity, "dato desmarcado", Toast.LENGTH_SHORT).show();


                    oneChecked = false;


                    HashMap<String, Object> mapCD = new HashMap<>();

                    mapCD.put("color", "Pendiente");

                    mapCD.put("pago", "en espera");



                    mFirestore.collection("comprasfinal/"+codigoidd+"/cliente").document(id).update(mapCD);

                    //PedidoFinalRepartidorAdapter.this.notifyDataSetChanged();





                    mFirestore.collection("comprasfinal/"+codigoidd+"/cliente").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Double precio_totalpt = documentSnapshot.getDouble("vaccine_price");



                            HashMap<String, Object> mapPE = new HashMap<>();


                            mapPE.put("precio_producto", precio_totalpt);



                            mFirestore.collection("comprasfinal/"+codigoidd+"/MensajePago").document(id).update(mapPE);





                        }
                    });













                }



            }
        });


















    }








    @NonNull
    @Override
    public PedidoFinalRepartidorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pedidofinalrepartidor_single, parent,false);

        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, nombre, color, vaccine_price, pago,hora;
        ImageView btn_edit;
        CheckBox check_box;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
            nombre = itemView.findViewById(R.id.nombrecliente);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);
            pago = itemView.findViewById(R.id.pago);
            hora = itemView.findViewById(R.id.hora);


            btn_edit = itemView.findViewById(R.id.btn_editar);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null){
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(position), position);


                    }
                }
            });



            check_box = itemView.findViewById(R.id.check_box);









        }







    }




    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot , int position);
    }


    public void setOnItemClickListener(PedidoFinalRepartidorAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;

    }




}
