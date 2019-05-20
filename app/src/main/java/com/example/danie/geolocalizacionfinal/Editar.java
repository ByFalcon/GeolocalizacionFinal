package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Editar extends AppCompatActivity {

    private TextView tvPuntosEditar;
    private EditText etNombreEditar, etComentarioEditar;
    private Button btMenosEditar, btMasEditar;

    Lugar l;

    GestorLugar gestor;

    private Firebase firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Intent i = getIntent();
        l = i.getParcelableExtra("lugarEditar");

        etNombreEditar.setText(l.getNombre());
        tvPuntosEditar.setText(l.getPuntuacion() + "");
        etComentarioEditar.setText(l.getComentario());

        btMenosEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntosEditar.getText().toString());
                if (puntuacion > 1) {
                    puntuacion--;
                    tvPuntosEditar.setText(String.valueOf(puntuacion));
                }
            }
        });

        btMasEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntosEditar.getText().toString());
                if (puntuacion < 5) {
                    puntuacion++;
                    tvPuntosEditar.setText(String.valueOf(puntuacion));
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.setNombre(etNombreEditar.getText().toString());
                l.setPuntuacion(Integer.parseInt(tvPuntosEditar.getText().toString()));
                l.setComentario(etComentarioEditar.getText().toString());
                Map<String, Object> saveItem = new HashMap<>();
                String key = l.getKey();
                saveItem.put("/usuarios/"+ firebaseUser.getUid() + "-" + firebaseUser.getDisplayName() +
                        "/lugar/" + key + "/", l.toMap());
                databaseReference.updateChildren(saveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Se ha editado en firebase", Toast.LENGTH_SHORT).show();
                        long nunmç = gestor.edit(l);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void init(){
        tvPuntosEditar = findViewById(R.id.textViewPuntosEditar);
        etNombreEditar = findViewById(R.id.editTextNombreEditar);
        etComentarioEditar = findViewById(R.id.editTextComentarioEditar);
        btMenosEditar = findViewById(R.id.buttonMenosEditar);
        btMasEditar = findViewById(R.id.buttonMasEditar);

        firebase = new Firebase(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = firebase.getUsuario();

        gestor = new GestorLugar(this, true);
    }

}
