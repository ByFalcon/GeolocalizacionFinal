package com.example.danie.geolocalizacionfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Add extends AppCompatActivity {

    //https://developer.android.com/training/location
    //https://github.com/googlesamples/android-play-location/blob/master/LocationUpdates/app/src/main/java/com/google/android/gms/location/sample/locationupdates/MainActivity.java

    private static final int PERMISO_GPS = 1;
    public static final String TAG = "ZZZ";

    //private AddressResultReceiver resultReceiver;
    //private FusedLocationProviderClient fusedLocationClient;
    //private Location ultimaPosicion = null;
    //private LocationCallback callback;
    //private LocationRequest request;

    private EditText editTextNombre;
    private EditText editTextComentario;
    private Button btSumar;
    private Button btRestar;
    private Button btPosicion;
    private TextView tvPuntuacion;

    Lugar lugar = new Lugar();

    /*class AddressResultReceiver extends ResultReceiver {

        //GestorLugar gestor = new GestorLugar(getApplicationContext());

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            Log.v("ZZZ", "OnReceiverResult");

            //Lugar lugar = resultData.getParcelable(ServicioGeocoder.Constants.RESULT_DATA_KEY);
            String resultado = resultData.getString(ServicioGeocoder.Constants.RESULT_DATA_KEY);

            goodDirection(resultado);
            //Log.v("ZZZ", "Se ha salido del metodo");

            /*Long num = gestor.add(lugar);
            if (num != -1) {
                Log.v("ZZZ", "Se ha insertado el lugar");
                Toast.makeText(getApplicationContext(), "Se ha realizado el insert", Toast.LENGTH_SHORT).show();
            }
            gestor.cerrar();
        }

        protected void goodDirection(String direccion){
            String[] partes = direccion.split(",");
            String localidad = "", pais = "";
            if(partes.length>=4) {
                localidad = partes[2];
                pais = partes[3];
            }
            String numeros = "0123456789";
            String localidadFiltrada = "";
            boolean letraCorrecta;
            for (int i = 0; i < localidad.length(); i++){
                letraCorrecta = true;
                for (int j = 0; j<numeros.length(); j++){
                    if (localidad.charAt(i)==numeros.charAt(j)){
                        letraCorrecta = false;
                    }
                }
                if (letraCorrecta==true){
                    localidadFiltrada+=localidad.charAt(i);
                }
            }
            lugar.setLocalidad(localidadFiltrada.trim());
            lugar.setPais(pais.trim());
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.v(TAG, "aquí debe saltar un elemento que explique la razón por la que se necesita el permiso");
            } else {
                Log.v(TAG, "aquí se solicita el permiso (normalmente la primera vez)");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISO_GPS);
            }
        } else {
            Log.v(TAG, "se dispone del permiso por lo que se lanza directamente");
        }

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
                lugar.setNombre(editTextNombre.getText().toString());
                lugar.setComentario(editTextComentario.getText().toString());
                lugar.setPuntuacion(Integer.parseInt(tvPuntuacion.getText().toString()));
                String date = dateNow();
                lugar.setFecha(date);
                //getLocation();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Intent i = new Intent();
                i.putExtra("lugar", lugar);
                setResult(Add.RESULT_OK, i);
                finish();
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
/*
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //resultReceiver = new AddressResultReceiver(new Handler());
        callback = createLocationCallback();
        request = createLocationRequest();

        fusedLocationClient.requestLocationUpdates(
                request,
                callback,
                null);
    }

    private LocationCallback createLocationCallback() {
        Log.v(TAG, "creando objeto que se ejecuta después de cada nueva Location");
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.v(TAG, "se ha recibido nueva Location");
                for (Location location : locationResult.getLocations()) {
                    ultimaPosicion = location;
                    Log.v(TAG, location.getLatitude() + " " + location.getLongitude());
                    lugar.setLatitud(location.getLatitude());
                    lugar.setLongitud(location.getLongitude());
                }
                requestAddress(ultimaPosicion);
                fusedLocationClient.removeLocationUpdates(callback);
            }
        };
        return locationCallback;
    }

    private void requestAddress(Location location) {
        Log.v("ZZZ", "Se ha entrado en el requestAddress");
        Intent intent = new Intent(this, ServicioGeocoder.class);
        //intent.putExtra(ServicioGeocoder.Constants.RECEIVER, resultReceiver);
        intent.putExtra(ServicioGeocoder.Constants.LOCATION_DATA_EXTRA, location);
        intent.putExtra("Lugar", lugar);
        startService(intent);
    }

    protected LocationRequest createLocationRequest() {
        Log.v(TAG, "creando solicitud de Locations");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISO_GPS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "con permiso");
                    //getLocation();
                } else {
                    Log.v(TAG, "sin permiso");
                }
            }
        }
    }*/

}
