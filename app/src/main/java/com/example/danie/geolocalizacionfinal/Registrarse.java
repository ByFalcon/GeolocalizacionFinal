package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrarse extends AppCompatActivity {

    private Button btIniciarSesionReg, btRegistrarseReg;
    private EditText etEmailReg, etContraReg;

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                finish();
            }
        });

        btRegistrarseReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (usuarioCorrecto()){
                    firebase.crearUsuario(etEmailReg.getText().toString(), etContraReg.getText().toString());
                    Intent iniSesion = new Intent(Registrarse.this, IniciarSesion.class);
                    iniSesion.putExtra("email", etEmailReg.getText().toString());
                    iniSesion.putExtra("contra", etContraReg.getText().toString());
                    startActivity(iniSesion);
                    finish();
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

    private boolean usuarioCorrecto() {
        if (etEmailReg.getText().toString().isEmpty() || etContraReg.getText().toString().isEmpty()) {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
            return false;
        } else if(comprobarEmail(etEmailReg.getText().toString())==false) {
            Toast.makeText(this, "Correo electronico incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etContraReg.getText().toString().length()<6) {
            Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean comprobarEmail(String email){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Inicial.class);
        startActivity(i);
        finish();
    }
}
