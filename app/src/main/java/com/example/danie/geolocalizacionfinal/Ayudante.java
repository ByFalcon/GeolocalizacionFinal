package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Ayudante extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lugar.sqlite";
    private static final int VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        //Log.v(LOG, "constructor ayudante");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.v(LOG, "oncreate basedatos");
        db.execSQL(Contrato.TableLugar.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.v(LOG, "onupgrade basedatos");
        db.execSQL(Contrato.TableLugar.DROP_TABLE);
        onCreate(db);
    }
}
