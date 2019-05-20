package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenciasCompartidas {

    private Context context;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    public PreferenciasCompartidas(Context context){
        this.context = context;
    }

    public void guardarUsuarioPC(String email, String contra){
        preferencias = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferencias.edit();
        editor.putString("credenciales", email +"-"+ contra);
        editor.apply();
    }

    public String getPreferencias() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String v = pref.getString("credenciales", "null");
        return v;
    }

    public void eliminarPreferencias() {
        preferencias = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferencias.edit();
        preferencias.edit().remove("credenciales").apply();
        this.editor = null;
    }
}
