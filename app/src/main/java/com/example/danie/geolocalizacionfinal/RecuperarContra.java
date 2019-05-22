package com.example.danie.geolocalizacionfinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContra extends AppCompatActivity {

    private EditText etEmailRestablecer;
    private Button btRestablecerRestablecer;
    Firebase firebase;
    FirebaseAuth autentificador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        btRestablecerRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmailRestablecer.getText().toString().isEmpty()){
                    restablecerContra(etEmailRestablecer.getText().toString());
                }else{
                    Toast.makeText(RecuperarContra.this, "Debe introducir un correo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(){
        etEmailRestablecer = findViewById(R.id.etEmailRestablecer);
        btRestablecerRestablecer = findViewById(R.id.btRestablecerRestablecer);

        FirebaseApp.initializeApp(this);
        firebase = new Firebase(getApplicationContext());
        autentificador=  FirebaseAuth.getInstance();
    }

    public void restablecerContra(String email){
        autentificador.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarContra.this, "Se ha enviado un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecuperarContra.this, "El correo no esta registrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
