package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IniciarSesion extends AppCompatActivity {

    private Button btRegisterIS, btIniciarSesionIS, btRecordarContra;
    private EditText etEmailIS, etContraIS;

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
    }

    private void init(){
        btIniciarSesionIS = findViewById(R.id.buttonIniciarSesionIS);
        btRegisterIS = findViewById(R.id.buttonRegistrarseIS);
        btRecordarContra = findViewById(R.id.buttonOlvidarContra);
        etEmailIS = findViewById(R.id.etEmailIS);
        etContraIS = findViewById(R.id.etContraIS);
    }
}
