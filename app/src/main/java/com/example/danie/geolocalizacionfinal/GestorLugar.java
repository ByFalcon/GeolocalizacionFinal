package com.example.danie.geolocalizacionfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GestorLugar {

    private Ayudante ayudante;
    private SQLiteDatabase bd;

    public GestorLugar(Context c) {
        this(c, true);
    }

    public GestorLugar(Context c, boolean write) {
        //Log.v(LOG, "constructor gestor");
        this.ayudante = new Ayudante(c);
        if (write) {
            bd = ayudante.getWritableDatabase();
        } else {
            bd = ayudante.getReadableDatabase();
        }
    }

    public void cerrar() {
        ayudante.close();
    }

    //crud
    //create -> add, insert, persist
    public long add(Lugar lugar) {
        //... objeto -> contentvalues
        return bd.insert(Contrato.TableLugar.TABLE, null, GestorLugar.get(lugar));
    }

    public long insert(Lugar lugar) {
        return add(lugar);
    }

    public long persist(Lugar lugar) {
        return add(lugar);
    }

    //read -> uno, todos, condicion + lista, cursor
    public List<Lugar> get() {
        //select all
        return get(null, null);
    }

    public List<Lugar> getLugar() {
        return get();
    }

    public List<Lugar> get(String condicion, String[] parametros) {
        //todos los lugares que cumplan esa condicion
        List<Lugar> todos = new ArrayList<>();
        Cursor cursor = getCursor(condicion, parametros);
        while (cursor.moveToNext()) {
            todos.add(get(cursor));
        }
        cursor.close();
        return todos;
    }

    public List<Lugar> getLugar(String condicion, String[] parametros) {
        return get(condicion, parametros);
    }

    public Lugar get(long id) {
        //devuelve un lugar
        Lugar l = null;
        List<Lugar> contactos = get(Contrato.TableLugar._ID + " = ?", new String[]{id + ""});
        if (contactos.size() > 0) {
            l = contactos.get(0);
        }
        return l;
    }

    public Lugar getLugar(long id) {
        return get(id);
    }

    public Cursor getCursor() {
        return getCursor(null, null);
    }

    public Cursor getCursor(String condicion, String[] parametros) {
        return bd.query(
                Contrato.TableLugar.TABLE,
                null,
                condicion,
                parametros,
                null,
                null,
                Contrato.TableLugar._ID + " desc");
    }

    public Cursor getCursor(long id) {
        return getCursor(Contrato.TableLugar._ID + " = ?", new String[]{id + ""});
    }

    //update -> edit - update - save
    public int edit(Lugar lugar) {
        //objeto -> content values
        return bd.update(
                Contrato.TableLugar.TABLE,
                GestorLugar.get(lugar),
                Contrato.TableLugar._ID + " = ?",
                new String[]{lugar.getId() + ""});
    }

    //delete -> delete, erase, remove
    public int remove(Lugar l) {
        return remove(l.getId());
    }

    public int remove(long id) {
        String condicion = Contrato.TableLugar._ID + " = ?";
        String[] argumentos = { id + "" };
        return bd.delete(Contrato.TableLugar.TABLE, condicion, argumentos);
    }

    public static Lugar get(Cursor c) {//devolver un objeto a partir de las columnas
        Lugar lugar = new Lugar();
        lugar.setId(c.getLong(c.getColumnIndex(Contrato.TableLugar._ID)));
        lugar.setNombre(c.getString(c.getColumnIndex(Contrato.TableLugar.NOMBRE)));
        lugar.setLatitud(c.getDouble(c.getColumnIndex(Contrato.TableLugar.LATITUD)));
        lugar.setLongitud(c.getDouble(c.getColumnIndex(Contrato.TableLugar.LONGITUD)));
        lugar.setLocalidad(c.getString(c.getColumnIndex(Contrato.TableLugar.LOCALIDAD)));
        lugar.setPais(c.getString(c.getColumnIndex(Contrato.TableLugar.PAIS)));
        lugar.setComentario(c.getString(c.getColumnIndex(Contrato.TableLugar.COMENTARIO)));
        lugar.setPuntuacion(c.getInt(c.getColumnIndex(Contrato.TableLugar.PUNTOS)));
        lugar.setFecha(c.getString(c.getColumnIndex(Contrato.TableLugar.FECHA)));
        return lugar;
    }

    public static Lugar getLugar(Cursor c){
        return GestorLugar.get(c);
    }

    private static ContentValues get(Lugar lugar) {
        ContentValues contentValues = new ContentValues();
        //contentValues.put(Contrato.TableLugar._ID, lugar.getId());
        contentValues.put(Contrato.TableLugar.NOMBRE, lugar.getNombre());
        contentValues.put(Contrato.TableLugar.LATITUD, lugar.getLatitud());
        contentValues.put(Contrato.TableLugar.LONGITUD, lugar.getLongitud());
        contentValues.put(Contrato.TableLugar.LOCALIDAD, lugar.getLocalidad());
        contentValues.put(Contrato.TableLugar.PAIS, lugar.getPais());
        contentValues.put(Contrato.TableLugar.COMENTARIO, lugar.getComentario());
        contentValues.put(Contrato.TableLugar.PUNTOS, lugar.getPuntuacion());
        contentValues.put(Contrato.TableLugar.FECHA, lugar.getFecha());
        return contentValues;
    }

    private static ContentValues getContentValues(Lugar lugar){
        return GestorLugar.get(lugar);
    }
}
