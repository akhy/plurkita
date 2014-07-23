package net.akhyar.plurkita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cengalabs.flatui.FlatUI;

import net.akhyar.plurkita.api.ErrorEvent;
import net.akhyar.plurkita.module.For;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * @author akhyar
 */
public class BaseActivity extends ActionBarActivity {

    @Inject
    @For("error")
    EventBus errorBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(
                this, R.array.plurk_theme, false, 2
        ));
        getSupportActionBar().setTitle(getTitle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        errorBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        errorBus.register(this);
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton.makeText(this, event.getMessage(), Style.ALERT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
