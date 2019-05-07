package com.example.danie.geolocalizacionfinal;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private static final int PERMISO_GPS = 1;
    public static final String TAG = "ZZZ";

    private AddressResultReceiver resultReceiver;
    private FusedLocationProviderClient fusedLocationClient;
    private Location ultimaPosicion = null;
    private LocationCallback callback;
    private LocationRequest request;

    FloatingActionButton fab;

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

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
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
            lugar = data.getParcelableExtra("lugarAdd");
            fab.setEnabled(false);
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
        Log.v(TAG, "Se ha entrado en el requestAddress");
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
            Lugar lugarInsertado = resultData.getParcelable("lugarInsertado");
            Map<String, Object> saveItem = new HashMap<>();
            String key = databaseReference.child("item").push().getKey();
            lugarInsertado.setKey(key);
            saveItem.put("/usurio/lugar/" + key + "/", lugarInsertado.toMap());
            databaseReference.updateChildren(saveItem)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Se ha insertado en firebase", Toast.LENGTH_LONG).show();
                        }
                    });

            long num = gestor.insert(lugarInsertado);
            //long num = resultData.getLong(ServicioGeocoder.Constants.RESULT_DATA_KEY);
            if (num > 0){
                Log.v(TAG, "Se ha entrado en el if del insert");

                /*
                sol 1
                Lugar l = resultData.getParcelable("lugarInsertado");
                lugares.add(l);
                adaptador.notifyDataSetChanged();
                */

                /* sol 2 */
                lugares = gestor.get();
                adaptador.swap(lugares);

                /*
                sol 3
                adacptador basado en el cursor
                */

                Toast.makeText(MainActivity.this, "Se ha insertado el lugar", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "No se ha insertado el lugar", Toast.LENGTH_LONG).show();
            }
            fab.setEnabled(true);
        }
    }
}
