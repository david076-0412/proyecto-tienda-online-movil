package com.aplication.appgestionrepartos.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.adapter.ContactosAdapter;
import com.aplication.appgestionrepartos.administrador.PanelChat_Administrador;
import com.aplication.appgestionrepartos.cliente.PanelChat_Cliente;
import com.aplication.appgestionrepartos.model.Usuarios;
import com.aplication.appgestionrepartos.no_internet_connection.NetworkChangeListener;
import com.aplication.appgestionrepartos.repartidor.PanelChat_RepartidorActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactosRepartidorActivity extends AppCompatActivity {


    Button btn_exit;
    ContactosAdapter mAdapter;

    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView search_view;
    Query query;

    NetworkChangeListener networkChangeListener;


    String Usuario;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_repartidor);




        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);

        btn_exit = findViewById(R.id.btn_close);


        Usuario = getIntent().getStringExtra("Us");


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Usuario.equals("Cliente")){

                    Intent ic = new Intent(ContactosRepartidorActivity.this, PanelChat_Cliente.class);

                    ic.putExtra("Us",Usuario);

                    startActivity(ic);



                }else if (Usuario.equals("Repartidor")){

                    Intent ir = new Intent(ContactosRepartidorActivity.this, PanelChat_RepartidorActivity.class);

                    ir.putExtra("Us",Usuario);

                    startActivity(ir);



                }else if (Usuario.equals("Administrador")){

                    Intent ia = new Intent(ContactosRepartidorActivity.this, PanelChat_Administrador.class);

                    ia.putExtra("Us",Usuario);


                    startActivity(ia);



                }



            }
        });


        setUpRecyclerView();



        search_view();






    }




    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {

        String id = mAuth.getUid();

        String idUser = mAuth.getCurrentUser().getUid();

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        query = mFirestore.collection("Repartidor");


        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>().setQuery(query, Usuarios.class).build();

        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ContactosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Usuarios usuarios = documentSnapshot.toObject(Usuarios.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();



                //Toast.makeText(ContactosAdministradorActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();



                Intent i = new Intent(ContactosRepartidorActivity.this, Chat_RepartidorActivity.class);


                mFirestore.collection("Usuario/Repartidor/"+id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            String nombrecliente = snapshot.getString("name");
                            String apellidocliente = snapshot.getString("apellido");
                            //Toast.makeText(ContactosClienteActivity.this, nombrecliente, Toast.LENGTH_SHORT).show();

                            UserDetails.chatWith = nombrecliente + " " + apellidocliente;

                            i.putExtra("name",nombrecliente);
                            i.putExtra("apellido", apellidocliente);
                            i.putExtra("Us",Usuario);


                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                            startActivity(i);


                        }
                    }
                });








            }
        });







    }





    private void search_view() {
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }



    public void textSearch(String s){
        FirestoreRecyclerOptions<Usuarios> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Usuarios>()
                        .setQuery(query.orderBy("name")
                                .startAt(s).endAt(s+"~"), Usuarios.class).build();
        mAdapter = new ContactosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }




    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();




    }
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();




    }
















}