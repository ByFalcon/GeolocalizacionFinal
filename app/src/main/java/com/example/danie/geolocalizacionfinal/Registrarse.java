package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrarse extends AppCompatActivity {

    private Button btIniciarSesionReg, btRegistrarseReg;
    private EditText etEmailReg, etContraReg;

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        btIniciarSesionReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IniciarSesion.class);
                startActivity(i);
            }
        });

        btRegistrarseReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioCorrecto()){
                    firebase.crearUsuario(etEmailReg.getText().toString(), etContraReg.getText().toString());

                    Intent iniSesion = new Intent(Registrarse.this, IniciarSesion.class);
                    iniSesion.putExtra("email", etEmailReg.getText().toString());
                    iniSesion.putExtra("contra", etContraReg.getText().toString());
                    startActivity(iniSesion);
                }
            }
        });

    }

    private void init(){
        btIniciarSesionReg = findViewById(R.id.buttonIniciarSesionReg);
        btRegistrarseReg = findViewById(R.id.buttonRegistrarseReg);
        etEmailReg = findViewById(R.id.etEmailReg);
        etContraReg = findViewById(R.id.etContraReg);

        FirebaseApp.initializeApp(this);
        firebase = new Firebase(getApplicationContext());
    }

    private boolean usuarioCorrecto(){
        if (etEmailReg.getText().toString().isEmpty() || etContraReg.getText().toString().isEmpty()){
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
