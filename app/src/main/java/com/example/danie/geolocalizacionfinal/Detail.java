package com.example.danie.geolocalizacionfinal;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class Detail extends AppCompatActivity {

    private TextView textViewDetalleNombre, textViewDetalleLocalidad, textViewDetallePais,
            textViewDetalleLatitud, textViewDetalleLongitud, textViewDetalleComentario, textViewDetalleFecha;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Intent intent = getIntent();
        Lugar lugar = intent.getParcelableExtra("lugarDetalle");
        Log.v("xxx", lugar.toString());

        textViewDetalleNombre.setText(lugar.getNombre());
        textViewDetalleLocalidad.setText(lugar.getLocalidad());
        textViewDetallePais.setText(lugar.getPais());
        textViewDetalleLatitud.setText(lugar.getLatitud() + "");
        textViewDetalleLongitud.setText(lugar.getLongitud() + "");
        textViewDetalleComentario.setText(lugar.getComentario());
        ratingBar.setRating(lugar.getPuntuacion());
        ratingBar.setFocusable(false);
        ratingBar.setEnabled(false);
        textViewDetalleFecha.setText(lugar.getFecha());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init(){
        textViewDetalleNombre = findViewById(R.id.textViewDetalleNombre);
        textViewDetalleLocalidad = findViewById(R.id.textViewDetalleLocalidad);
        textViewDetallePais = findViewById(R.id.textViewDetallePais);
        textViewDetalleLatitud = findViewById(R.id.textViewDetalleLatitud);
        textViewDetalleLongitud = findViewById(R.id.textViewDetalleLongitud);
        textViewDetalleComentario = findViewById(R.id.textViewDetalleComentario);
        ratingBar = findViewById(R.id.ratingBar);
        textViewDetalleFecha = findViewById(R.id.textViewDetalleFecha);
    }

}
