package com.aplication.appgestionrepartos.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.Pedido;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;


public class PedidoAdapter extends FirestoreRecyclerAdapter<Pedido, PedidoAdapter.ViewHolder> {

    private PedidoAdapter.OnItemClickListener onItemClickListener;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;





    public PedidoAdapter(@NonNull FirestoreRecyclerOptions<Pedido> options, Activity activity, FragmentManager fm){
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder viewHolder, int i, @NonNull Pedido Pedido) {
        DecimalFormat format = new DecimalFormat("0.00");
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        final String id = documentSnapshot.getId();

        viewHolder.name.setText(Pedido.getName());
        viewHolder.codigo.setText(Pedido.getCodigo());
        viewHolder.color.setText(Pedido.getColor());
        viewHolder.vaccine_price.setText("S/."+format.format(Pedido.getVaccine_price()));


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pedido_single, parent,false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, codigo, color, vaccine_price;

        public ViewHolder(@NonNull View itemView){
           super(itemView);
            name = itemView.findViewById(R.id.nombre);
            codigo = itemView.findViewById(R.id.codigoidd);
            color = itemView.findViewById(R.id.color);
            vaccine_price = itemView.findViewById(R.id.precio_vacuna);


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

    public void setOnItemClickListener(PedidoAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;

    }





}
