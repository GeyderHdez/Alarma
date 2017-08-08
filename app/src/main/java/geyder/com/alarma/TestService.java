package geyder.com.alarma;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by geyder on 23/07/17.
 */

public class TestService extends IntentService {

    public TestService() {
        super("MyTestService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "Alarma probada", Toast.LENGTH_LONG).show();
        Log.i("TestService", "Servicio ejecutandose. Recordatorios");
    }
}
