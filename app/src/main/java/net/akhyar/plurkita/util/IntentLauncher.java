package net.akhyar.plurkita.util;

import android.content.Context;
import android.content.Intent;

/**
 * @author akhyar
 */
public class IntentLauncher {

    private Context context;
    private Intent intent;

    public IntentLauncher(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void startActivity() {
        context.startActivity(intent);
    }

    public void startService() {
        context.startService(intent);
    }
}
