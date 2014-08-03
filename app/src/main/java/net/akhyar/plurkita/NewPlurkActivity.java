package net.akhyar.plurkita;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import net.akhyar.plurkita.api.ErrorEvent;
import net.akhyar.plurkita.api.TimelineApi;
import net.akhyar.plurkita.event.PlurkAdded;
import net.akhyar.plurkita.model.Plurk;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @author akhyar
 */
public class NewPlurkActivity extends BaseActivity {

    @Inject
    TimelineApi timelineApi;
    @Inject
    EventBus eventBus;
    @InjectView(R.id.contentText)
    EditText contentText;

    private Action1<Throwable> errorHandler = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            errorBus.post(new ErrorEvent(throwable));
        }
    };
    private MenuItem send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plurk);

        Application.inject(this);
        ButterKnife.inject(this);

        ActionBar ab = getSupportActionBar();
        ab.setIcon(getResources().getDrawable(R.drawable.ic_action_back));
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.transparent);

        // TODO handle intent
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_plurk, menu);
        send = menu.findItem(R.id.action_send);
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
        setSupportProgressBarIndeterminateVisibility(true);
        send.setVisible(false);
        timelineApi.addPlurk(":", contentText.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Plurk>() {
                            @Override
                            public void call(Plurk plurk) {
                                eventBus.post(new PlurkAdded(plurk));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                errorHandler.call(throwable);

                                setSupportProgressBarIndeterminateVisibility(false);
                                send.setVisible(true);
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                setSupportProgressBarIndeterminateVisibility(false);
                                send.setVisible(true);
                                finish();
                            }
                        }
                );
    }
}
