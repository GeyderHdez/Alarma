package geyder.com.alarma;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.*;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by geyder on 22/07/17.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Alarma( id INTEGER PRIMARY KEY AUTOINCREMENT, encabezado TEXT, mensaje TEXT, fecha DATE, hora TIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Alarma" );
        db.execSQL("CREATE TABLE Alarma( id INTEGER PRIMARY KEY AUTOINCREMENT, encabezado TEXT, mensaje TEXT, Fecha DATE, hora TIME)");
    }
}
