package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Inicial extends AppCompatActivity {

    private Button btSingIn, btRegister;
    private Intent intent;
    PreferenciasCompartidas preferenciasCompartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if(preferenciasCompartidas.getPreferencias() != null && preferenciasCompartidas.getPreferencias() != "null"){
            Intent i = new Intent(this, IniciarSesion.class);
            startActivity(i);
            finish();
        }

        btSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), IniciarSesion.class);
                startActivity(intent);
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Registrarse.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init(){
        btSingIn = findViewById(R.id.buttonSingIn);
        btRegister = findViewById(R.id.buttonRegister);
        preferenciasCompartidas = new PreferenciasCompartidas(getApplicationContext());
    }
}
