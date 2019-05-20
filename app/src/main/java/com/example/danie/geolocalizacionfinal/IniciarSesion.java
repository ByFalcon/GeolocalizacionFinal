package com.example.danie.geolocalizacionfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {

    private Button btRegisterIS, btIniciarSesionIS, btRecordarContra;
    private EditText etEmailIS, etContraIS;
    private CheckBox checkBox;
    private Firebase firebase;
    private FirebaseAuth firebaseAuth;
    PreferenciasCompartidas preferenciasCompartidas;

    private String emailPreferencias, contraPreferencias;

    private AlertDialog.Builder dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Intent i  = getIntent();

        String email = i.getStringExtra("email");
        String contra = i.getStringExtra("contra");

        etEmailIS.setText(email);
        etContraIS.setText(contra);

        if(preferenciasCompartidas.getPreferencias() != null){
            String s = preferenciasCompartidas.getPreferencias();
            String[] array = s.split("-");
            try{
                emailPreferencias = array[0];
                contraPreferencias = array[1];
                iniciarSesion(emailPreferencias, contraPreferencias);
            }catch (ArrayIndexOutOfBoundsException ex){}
        }

        btIniciarSesionIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmailIS.getText().toString().isEmpty() && !etContraIS.getText().toString().isEmpty()){
                    iniciarSesion(etEmailIS.getText().toString(), etContraIS.getText().toString());
                }else{
                    Toast.makeText(IniciarSesion.this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        });

        btRegisterIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registrarse.class);
                startActivity(i);
            }
        });

        btRecordarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RecuperarContra.class);
                startActivity(i);
            }
        });
    }

    private void init(){
        btIniciarSesionIS = findViewById(R.id.buttonIniciarSesionIS);
        btRegisterIS = findViewById(R.id.buttonRegistrarseIS);
        btRecordarContra = findViewById(R.id.buttonOlvidarContra);
        etEmailIS = findViewById(R.id.etEmailIS);
        etContraIS = findViewById(R.id.etContraIS);
        checkBox = findViewById(R.id.checkBox);

        preferenciasCompartidas = new PreferenciasCompartidas(getApplicationContext());

        FirebaseApp.initializeApp(this);
        firebase = new Firebase(getApplicationContext());
        firebaseAuth=  FirebaseAuth.getInstance();
    }

    public void iniciarSesion(String email, String contra){
        final String finalEmail = email;
        final String  finalPass=contra;

        firebaseAuth.signInWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialogoIniciarSesion(dialogo);
                    Log.v("ZZZ", "Task successful");

                    if (checkBox.isChecked()){
                        preferenciasCompartidas.guardarUsuarioPC(finalEmail, finalPass);
                    }
                    Intent i = new Intent(IniciarSesion.this, MainActivity.class);
                    startActivity(i);
                } else {
                    try{
                        preferenciasCompartidas.eliminarPreferencias();
                    } catch (NullPointerException ex){
                    }
                    Toast.makeText(IniciarSesion.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void cancelar(){

    }

    public void dialogoIniciarSesion(AlertDialog.Builder dialogo){
        dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Iniciar sesión");
        dialogo.setMessage("Iniciando sesión...");
        dialogo.setCancelable(false);
        /*dialogo.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });*/
        dialogo.show();
    }
}
