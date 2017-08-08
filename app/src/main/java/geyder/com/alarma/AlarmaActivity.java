package geyder.com.alarma;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by geyder on 22/07/17.
 */

public class AlarmaActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextMensaje,editTextFecha, editTextHora;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private ContentValues registro;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private Button btnChangeDate, btnChangeTime ;
    private int year, month, day, minute, hour;

    private static final int DATE_DIALOG_ID = 999;
    private static final int TIME_DIALOG_ID = 998;

    Calendar calendario = Calendar.getInstance();
    int hora, min, dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);
        setCurrentDateOnView();
        addListenerOnButton();
        // hora
        setCurrentTimeOnView();
        editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        editTextMensaje = (EditText) findViewById(R.id.editTextMensaje);
        editTextFecha = (EditText) findViewById(R.id.editTextFecha);
        editTextHora = (EditText) findViewById(R.id.editTextHora);
        Alarma();
    }

    public void Alarma() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        int intervalMillis = 1000 * 60; //1 minuto

        AlarmManager alarma = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarma.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pendingIntent);
    }

    public void llenarBD(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, vars.bd, null, vars.version);
        bd = admin.getWritableDatabase();
        registro = new ContentValues();
        registro.put("encabezado", editTextTitulo.getText().toString());
        registro.put("mensaje", editTextMensaje.getText().toString());//nombre del campo
        registro.put("fecha", editTextFecha.getText().toString());
        registro.put("hora", editTextHora.getText().toString());
        bd.insert("Alarma", null, registro);//nombre de la tabla
        bd.close();
        editTextTitulo.setText("");
        editTextMensaje.setText("");
        editTextFecha.setText("");
        editTextHora.setText("");
        Toast.makeText(this, "Alarma Registrada", Toast.LENGTH_LONG).show();
    }

    //region collapse
    public void setCurrentTimeOnView() {

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

    public void setCurrentDateOnView() {

        datePicker = (DatePicker) findViewById(R.id.datePicker);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        // set current date into datepicker
        datePicker.init(year, month, day, null);
    }


    public void addListenerOnButton() {
        // fecha
        btnChangeDate = (Button) findViewById(R.id.buttonFecha);
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        // hora
        btnChangeTime = (Button) findViewById(R.id.buttonHora);
        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener, hour, minute,false);
        }
        return null;
    }

    // hora
    private TimePickerDialog.OnTimeSetListener timePickerListener =  new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

            hour = selectedHour;
            minute = selectedMinute;
            editTextHora.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    };

    // hora
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            // set selected date into textview
            editTextFecha.setText(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year));
            // set selected date into datepicker also
            datePicker.init(year, month, day, null);
        }
    };

    private static String padding_str(int time) {

        if (time >= 10)
            return String.valueOf(time);
        else
            return "0" + String.valueOf(time);
    }

    //endregion

}
