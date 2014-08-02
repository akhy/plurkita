package net.akhyar.plurkita;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import net.akhyar.plurkita.api.TimelineApi;

import javax.inject.Inject;

/**
 * @author akhyar
 */
public class NewPlurkActivity extends BaseActivity {

    @Inject
    TimelineApi timelineApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plurk);
        Application.inject(this);

        ActionBar ab = getSupportActionBar();
        ab.setIcon(getResources().getDrawable(R.drawable.ic_action_back));
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.transparent);

        // TODO handle intent
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_plurk, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                send();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void send() {
    }
}
