package com.example.danie.geolocalizacionfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISO_GPS = 1;
    public static final String TAG = "ZZZ";

    private AddressResultReceiver resultReceiver;
    private FusedLocationProviderClient fusedLocationClient;
    private Location ultimaPosicion = null;
    private LocationCallback callback;
    private LocationRequest request;

    private EditText editTextNombre;
    private EditText editTextComentario;
    private Button btSumar;
    private Button btRestar;
    private Button btPosicion;
    private TextView tvPuntuacion;

    Lugar lugar = new Lugar();

    static final int PREINSERT = 1;
    List<Lugar> lugares = new ArrayList<Lugar>();
    GestorLugar gestor;
    Ayudante ayudante;
    RecyclerView recyclerView;
    Adaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Add.class);
                startActivityForResult(intent, PREINSERT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ayudante = new Ayudante(this);
        gestor = new GestorLugar(this, true);

        lugares = gestor.get();

        adaptador = new Adaptador(lugares);
        recyclerView.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Detail.class);
                Lugar lugar = lugares.get(recyclerView.getChildAdapterPosition(v));
                i.putExtra("lugarDetalle", lugar);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PREINSERT && resultCode == RESULT_OK) {
            lugar = data.getParcelableExtra("lugar");
            Log.v("CCC", "getLocation()");
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        resultReceiver = new AddressResultReceiver(new Handler());
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
        intent.putExtra(ServicioGeocoder.Constants.RECEIVER, resultReceiver);
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
    }

    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            long r = resultData.getLong(ServicioGeocoder.Constants.RESULT_DATA_KEY);
            if (r>0){
                adaptador.notifyDataSetChanged();
            }
        }

    }
}
