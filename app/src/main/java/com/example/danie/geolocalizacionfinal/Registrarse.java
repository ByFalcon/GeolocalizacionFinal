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

public class Registrarse extends AppCompatActivity {

    private Button btIniciarSesionReg, btRegistrarseReg;
    private EditText etEmailReg, etContraReg;

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

    }

    private void init(){
        btIniciarSesionReg = findViewById(R.id.buttonIniciarSesionReg);
        btRegistrarseReg = findViewById(R.id.buttonRegistrarseReg);
    }
}
