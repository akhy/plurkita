package net.akhyar.plurkita;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import net.akhyar.plurkita.api.ErrorEvent;
import net.akhyar.plurkita.api.TimelineApi;
import net.akhyar.plurkita.event.TimelineUpdated;
import net.akhyar.plurkita.model.Plurk;
import net.akhyar.plurkita.model.PlurkViewHolder;
import net.akhyar.plurkita.model.Timeline;
import net.akhyar.plurkita.model.User;

import java.util.Iterator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class TimelineActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    Timber.Tree log;
    @Inject
    EventBus eventBus;
    @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.list)
    ListView list;

    PlurkAdapter adapter;

    private Action1<Throwable> errorHandler = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            errorBus.post(new ErrorEvent(throwable));
            swipeContainer.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Application.inject(this);
        ButterKnife.inject(this);
        eventBus.register(this);

        adapter = new PlurkAdapter(this, null, false);
        list.setAdapter(adapter);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorScheme(
                R.color.tertiary_lightest,
                R.color.complementary_lighter,
                R.color.tertiary_lighter,
                R.color.complementary_lightest
        );

        onEvent(new TimelineUpdated());
    }

    public void onEvent(TimelineUpdated ignored) {
        String sql = new Select().from(Plurk.class).orderBy("posted DESC").toSql();
        Cursor cursor = ActiveAndroid.getDatabase().rawQuery(sql, null);

        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.list)
    public void openComments(long id) {
        ResponsesActivity.create(this, id).startActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Override
    public void onRefresh() {
        Application.getInstance(TimelineApi.class).getPlurks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Timeline, Timeline>() {
                    @Override
                    @DebugLog
                    public Timeline call(Timeline timeline) {
                        ActiveAndroid.beginTransaction();
                        Iterator<User> iterator = timeline.getPlurkUsers().values().iterator();
                        for (; iterator.hasNext(); ) {
                            User user = iterator.next();
                            User.upsert(user);
                        }
                        for (Plurk plurk : timeline.getPlurks())
                            Plurk.upsert(plurk);

                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
                        return timeline;
                    }
                })
                .subscribe(new Action1<Timeline>() {
                    @Override
                    public void call(Timeline timeline) {
                        swipeContainer.setRefreshing(false);
                        eventBus.post(new TimelineUpdated());
                    }
                }, errorHandler);
    }


    private class PlurkAdapter extends CursorAdapter {

        public PlurkAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public PlurkAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            PlurkViewHolder holder = new PlurkViewHolder();
            return holder.createView(context, parent);
//            view.setTag(holder);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Plurk plurk = new Plurk();
            plurk.loadFromCursor(cursor);

            PlurkViewHolder holder = (PlurkViewHolder) view.getTag();
            holder.bind(cursor.getPosition(), plurk);
        }

//        @Override
//        public void notifyDataSetChanged() {
//            setList(new Select().from(Plurk.class).<Plurk>execute());
//            super.notifyDataSetChanged();
//        }
    }

}
