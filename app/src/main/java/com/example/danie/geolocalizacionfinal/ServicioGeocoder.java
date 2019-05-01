package com.example.danie.geolocalizacionfinal;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.List;
import java.util.Locale;

public class ServicioGeocoder extends IntentService {

    private static final String TAG = "ZZZ";
    protected ResultReceiver receiver;

    Lugar lugar = new Lugar();

    GestorLugar gestor;

    public final class Constants {
        public static final int SUCCES_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    }

    public ServicioGeocoder(){
        super("SercicioGeocoder");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.v("ZZZ", "Se ha iniciado el servicio");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if(intent == null) {
            return;
        }
        //String errorMessage = "";

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        lugar = intent.getParcelableExtra("Lugar");
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        Log.v("ZZZ", "Se ha asignado el lugar 2 al 1");

        List<Address> addresses = null;
        try{
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    10);
        } catch (IOException ioException) {
            Log.v("ZZZ", "Servicio no disponible");
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.v("ZZZ", "Geolocalizacion no valida");
        }

        Log.v("ZZZ", "Se ha llenado la lista de direcciones");

        if(addresses == null || addresses.size() == 0){
            Log.v("ZZZ", "No hay direccion");
        } else{
            Address address = addresses.get(0);
            String resultado = "";
            for (Address address1: addresses){
                resultado = "";
                for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++){
                    resultado+="\n" + address1.getAddressLine(i);
                    Log.v("direcciones1", address1.getAddressLine(i));
                }

            }
            resultado = "";
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                resultado += "\n" + address.getAddressLine(i);
                Log.v("direcciones", address.getAddressLine(i));
            }

            Log.v("ZZZ", "Lista de direcciones lista");

            goodDirection(resultado);

            deliverResultToReceiver(Constants.SUCCES_RESULT, resultado);
        }
    }

    protected void goodDirection(String direccion){
        String[] partes = direccion.split(",");
        String localidad = "", pais = "";
        if (partes.length>=5){
            localidad = partes[2].trim() + ", " + partes[3].trim();
            pais = partes[4];
        } else if (partes.length>=4) {
            localidad = partes[2].trim();
            pais = partes[3].trim();
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

    private void deliverResultToReceiver(int resultCode, String resultado){
        Log.v("ZZZ", "Se ha entrado en el deliverResultToReceiver");
        gestor = new GestorLugar(this);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long num = gestor.insert(lugar);
        if (num > 0){
            Log.v(TAG, "se ha insertado");
        }
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.RESULT_DATA_KEY, num);
        receiver.send(resultCode,bundle);
    }
}
