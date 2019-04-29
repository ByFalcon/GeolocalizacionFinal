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

    protected ResultReceiver receiver;

    //Lugar lugar = new Lugar();

    //GestorLugar gestor;

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
        //Lugar lugar2 = intent.getParcelableExtra("Lugar");
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        //lugar = lugar2;
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

            /*String[] partes = resultado.split(",");
            String localidad = partes[2];
            String pais = partes[3];

            Log.v("ZZZ", localidad);
            Log.v("ZZZ", pais);

            guardarDireccion(localidad.trim(), pais.trim());
            Log.v("ZZZ", "Ha terminado el metodo guardar direccion");
            */

            deliverResultToReceiver(Constants.SUCCES_RESULT, resultado);
        }
    }

    /*private void guardarDireccion(String localidad, String pais) {
        Log.v("ZZZ", "Metodo guardarDireccion");
        Log.v("ZZZ", "Localidad: " + localidad);
        Log.v("ZZZ", "Pais: " + pais);
        String numeros = "0123456789";
        String localidadFiltrada = "";
        boolean letraCorrecta;
        for (int i = 0; i<localidad.length(); i++){
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
        Log.v("ZZZ", "Localidad filtrada: " + localidadFiltrada.trim());
        lugar.setLocalidad(localidadFiltrada.trim());
        lugar.setPais(pais.trim());
    }*/

    private void deliverResultToReceiver(int resultCode, String resultado){
        Log.v("ZZZ", "Se ha entrado en el deliverResultToReceiver");
        Bundle bundle = new Bundle();
        //bundle.putParcelable(Constants.RESULT_DATA_KEY, lugar);
        bundle.putString(Constants.RESULT_DATA_KEY, resultado);
        receiver.send(resultCode, bundle);
    }
}
