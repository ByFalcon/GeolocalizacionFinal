package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private final String TAG = "ZZZ";
    private FirebaseAuth autentificador;
    private FirebaseUser usuario;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Context contexto;

    public Firebase(Context c) {
        contexto = c;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        autentificador = FirebaseAuth.getInstance();
    }

    public void cerrarSesion() {
        autentificador.signOut();
    }


    public boolean usuarioLogueado() {
        FirebaseUser user = autentificador.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public FirebaseUser getUsuario() {
        return autentificador.getCurrentUser();
    }

    public void crearUsuario(String email, String password) {
        autentificador.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user1 = autentificador.getCurrentUser();
                            Toast.makeText(contexto, "Usuario registrado", Toast.LENGTH_SHORT).show();
                            System.out.println("Usuario registrado " + user1.getUid() + user1.getEmail());
                        } else {
                            Log.v(TAG, task.getException().toString());
                            System.out.println("ERROR");
                        }
                    }
                });
    }

    public void guardarLugar(Lugar l){

    }

    public void editarLugar(Lugar l){

    }

    public void eliminarLectura(Lugar l){

    }
}
