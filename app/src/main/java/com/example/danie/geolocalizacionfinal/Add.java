package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Add extends AppCompatActivity {

    public static final String TAG = "ZZZ";

    private EditText editTextNombre;
    private EditText editTextComentario;
    private Button btSumar;
    private Button btRestar;
    private Button btPosicion;
    private TextView tvPuntuacion;

    Lugar lugar = new Lugar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        btSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntuacion.getText().toString());
                if (puntuacion < 5) {
                    puntuacion++;
                    tvPuntuacion.setText(String.valueOf(puntuacion));
                }
            }
        });

        btRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntuacion.getText().toString());
                if (puntuacion > 1) {
                    puntuacion--;
                    tvPuntuacion.setText(String.valueOf(puntuacion));
                }
            }
        });

        btPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextNombre.getText().toString().isEmpty()) {
                    lugar.setNombre(editTextNombre.getText().toString().trim());
                    lugar.setComentario(editTextComentario.getText().toString().trim());
                    lugar.setPuntuacion(Integer.parseInt(tvPuntuacion.getText().toString()));
                    String date = dateNow();
                    lugar.setFecha(date);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Intent i = new Intent();
                    i.putExtra("lugarAdd", lugar);
                    setResult(Add.RESULT_OK, i);
                    finish();
                }else{
                    Toast.makeText(Add.this, "Debes introducir un nombre", Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                setResult(Add.RESULT_CANCELED, i);
                finish();
            }
        });
    }

    private void init() {
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextComentario = findViewById(R.id.editTextComentario);
        tvPuntuacion = findViewById(R.id.textViewPuntuacion);
        btRestar = findViewById(R.id.buttonRestar);
        btSumar = findViewById(R.id.buttonSumar);
        btPosicion = findViewById(R.id.btPosicion);
    }

    public String dateNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
