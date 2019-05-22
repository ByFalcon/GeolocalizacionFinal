package com.example.danie.geolocalizacionfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Detail extends AppCompatActivity {

    private TextView textViewDetalleNombre, textViewDetalleLocalidad, textViewDetallePais,
            textViewDetalleLatitud, textViewDetalleLongitud, textViewDetalleComentario, textViewDetalleFecha;
    private RatingBar ratingBar;

    GestorLugar gestor;

    Lugar lugar;

    private Firebase firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        Intent intent = getIntent();
        lugar = intent.getParcelableExtra("lugarDetalle");

        textViewDetalleNombre.setText(lugar.getNombre());
        textViewDetalleLocalidad.setText(lugar.getLocalidad());
        textViewDetallePais.setText(lugar.getPais());
        textViewDetalleLatitud.setText(lugar.getLatitud() + "");
        textViewDetalleLongitud.setText(lugar.getLongitud() + "");
        textViewDetalleComentario.setText(lugar.getComentario());
        ratingBar.setRating(lugar.getPuntuacion());
        ratingBar.setEnabled(false);
        textViewDetalleFecha.setText(lugar.getFecha());
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

        gestor = new GestorLugar(this, true);

        firebase = new Firebase(getApplicationContext());
        firebaseUser = firebase.getUsuario();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.eliminar_lugar) {
            dialogoConfirmar();
            return true;
        }
        if (id == R.id.editar_lugar) {
            Intent i = new Intent(this, Editar.class);
            i.putExtra("lugarEditar", lugar);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void dialogoConfirmar(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¿Eliminar lugar?");
        dialogo.setMessage("Eliminará este lugar de forma permanente.");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                aceptar();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });
        dialogo.show();
    }

    private void aceptar() {
        Intent i = new Intent();
        String url = "/usuarios/"+ firebaseUser.getUid() + "-" + firebaseUser.getDisplayName() +
        "/lugar/" + lugar.getKey();
        databaseReference = firebaseDatabase.getReference(url);
        databaseReference.removeValue();
        gestor.remove(lugar.getKey());
        setResult(RESULT_OK, i);
        finish();
    }

    private void cancelar() {
        //finish();
    }
}
