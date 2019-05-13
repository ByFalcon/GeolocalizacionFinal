package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {

    private Button btRegisterIS, btIniciarSesionIS, btRecordarContra;
    private EditText etEmailIS, etContraIS;
    private CheckBox checkBox;
    private Firebase firebase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    PreferenciasCompartidas preferenciasCompartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        btRegisterIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registrarse.class);
                startActivity(i);
            }
        });

        btRecordarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear actividad para recordar contraseña
                //...
            }
        });

        btIniciarSesionIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init(){
        btIniciarSesionIS = findViewById(R.id.buttonIniciarSesionIS);
        btRegisterIS = findViewById(R.id.buttonRegistrarseIS);
        btRecordarContra = findViewById(R.id.buttonOlvidarContra);
        etEmailIS = findViewById(R.id.etEmailIS);
        etContraIS = findViewById(R.id.etContraIS);
        checkBox = findViewById(R.id.checkBox);

        preferenciasCompartidas = new PreferenciasCompartidas(getApplicationContext());

        FirebaseApp.initializeApp(this);
        firebase = new Firebase(getApplicationContext());
        firebaseAuth=  FirebaseAuth.getInstance();
    }
}
