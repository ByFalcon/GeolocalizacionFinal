package com.example.danie.geolocalizacionfinal;

import android.provider.BaseColumns;

public class Contrato {
    private Contrato() {
    }

    public static class TableLugar implements BaseColumns {

        public static final String TABLE = "lugar";
        public static final String NOMBRE = "nombre";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String LOCALIDAD = "localidad";
        public static final String PAIS = "pais";
        public static final String COMENTARIO = "comentario";
        public static final String PUNTOS = "puntos";
        public static final String FECHA = "fecha";

        public static final String CREATE_TABLE =
                "create table " + TABLE + " (" +
                        _ID + " integer primary key," +
                        NOMBRE + " text," +
                        LATITUD + " real," +
                        LONGITUD + " real," +
                        LOCALIDAD + " text," +
                        PAIS + " text," +
                        COMENTARIO + " text," +
                        PUNTOS + " integer," +
                        FECHA + " text)";

        public static final String DROP_TABLE =
                "drop table " + TABLE;
    }
}
