package geyder.com.alarma;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by geyder on 22/07/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1010;
    private AdminSQLiteOpenHelper admin;
    private Cursor cursor;
    private SQLiteDatabase bd;
    private String alarma, descripcion, titulo;

    @Override
    public void onReceive(Context context, Intent intent) {


        //intent de prueba
        Intent i = new Intent(context, TestService.class);
        context.startService(i);

        Calendar calendario = Calendar.getInstance();
        int hora, min,dia,mes,anio;
        String fecha_sistema, hora_sistema;

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);

        fecha_sistema = mes + 1 + "-" + dia + "-" + anio;
        hora_sistema = hora + ":" + min;
        admin = new AdminSQLiteOpenHelper(context, vars.bd, null, vars.version);
        bd = admin.getWritableDatabase();

        if(bd != null) {
            cursor = bd.rawQuery("SELECT * FROM Alarma WHERE fecha ='" + fecha_sistema + "' AND hora= '" + hora_sistema + "'", null);
            if(cursor.moveToFirst()){
                alarma = cursor.getString(0);
                titulo = cursor.getString(1);
                descripcion = cursor.getString(2);
                triggerNotification(context, titulo + "\n" + descripcion + alarma);
            }
        }
        bd.close();
    }

    private void triggerNotification(Context contexto, String texto) {
        Intent notificationIntent = new Intent(contexto, InfoActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(contexto, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setContentIntent(contentIntent)
                .setTicker("" )
                .setContentTitle("alarma")
                .setContentTitle("")
                .setContentText(texto)
                .setContentInfo("Info")
                .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.drawable.ic_announcement))
                .setSmallIcon(R.drawable.ic_announcement)
                .setAutoCancel(true) //Cuando se pulsa la notificación ésta desaparece
                .setSound(defaultSound)
                .setVibrate(pattern);

        Notification notificacion = new NotificationCompat.BigTextStyle(builder)
                .bigText(texto)
                .setBigContentTitle("Notificación")
                //.setSummaryText("Resumen de tareas")
                .build();

        notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificacion);

    }
}
