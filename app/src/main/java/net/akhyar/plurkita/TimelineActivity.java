package net.akhyar.plurkita;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.dd.processbutton.iml.ActionProcessButton;

import net.akhyar.plurkita.api.ErrorEvent;
import net.akhyar.plurkita.api.TimelineApi;
import net.akhyar.plurkita.event.ResponseAdded;
import net.akhyar.plurkita.event.TimelineUpdated;
import net.akhyar.plurkita.model.Plurk;
import net.akhyar.plurkita.model.PlurkViewHolder;
import net.akhyar.plurkita.model.Timeline;
import net.akhyar.plurkita.model.User;

import java.util.Iterator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class TimelineActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    EventBus eventBus;
    @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.list)
    ListView list;

    PlurkAdapter adapter;
    ActionProcessButton loadMore;

    private Func1<Timeline, Timeline> savePlurk = new Func1<Timeline, Timeline>() {
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
    };
    private Action1<Timeline> timelineAction = new Action1<Timeline>() {
        @Override
        public void call(Timeline timeline) {
            eventBus.post(new TimelineUpdated());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Application.inject(this);
        ButterKnife.inject(this);
        eventBus.register(this);

        View footer = getLayoutInflater().inflate(R.layout.item_loadmore, null);
        loadMore = (ActionProcessButton) footer.findViewById(R.id.loadMore);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadMore();
            }
        });

        adapter = new PlurkAdapter(this, null, false);
        list.addFooterView(footer);
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

    public void onLoadMore() {
        Plurk lastItem = adapter.getLastItem();
        if (lastItem == null)
            onRefresh();
        else {
            ButtonUtil.setState(loadMore, ButtonUtil.STATE_PROCESS, true);
            String offset = lastItem.getTimestampForOffset();
            Application.getInstance(TimelineApi.class).getPlurks(offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(savePlurk)
                    .subscribe(
                            timelineAction,
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    errorBus.post(new ErrorEvent(throwable));
                                    ButtonUtil.setState(loadMore, ButtonUtil.STATE_ERROR, true);
                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                    ButtonUtil.setState(loadMore, ButtonUtil.STATE_NORMAL, true);
                                }
                            }
                    );
        }
    }

    public void onEvent(TimelineUpdated ignored) {
        reloadTimeline();
    }

    @Override
    public void onRefresh() {
        Application.getInstance(TimelineApi.class).getPlurks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(savePlurk)
                .subscribe(
                        timelineAction,
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                errorBus.post(new ErrorEvent(throwable));
                                swipeContainer.setRefreshing(false);
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                swipeContainer.setRefreshing(false);
                            }
                        }
                );
    }

    private void reloadTimeline() {
        String sql = new Select().from(Plurk.class).orderBy("posted DESC").toSql();
        Observable.just(sql)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Cursor>() {
                    @Override
                    public Cursor call(String sql) {
                        return ActiveAndroid.getDatabase().rawQuery(sql, null);
                    }
                })
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        adapter.changeCursor(cursor);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void onEvent(ResponseAdded ignored) {
        reloadTimeline();
    }

    @OnClick(R.id.newPlurk)
    public void newPlurk() {
        startActivity(new Intent(this, NewPlurkActivity.class));
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

    private class PlurkAdapter extends CursorAdapter {

        public PlurkAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public PlurkAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        public Plurk getLastItem() {
            long id = getItemId(getCount() - 1);
            return Plurk.find(id);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            PlurkViewHolder holder = new PlurkViewHolder();
            return holder.createView(context, parent);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Plurk plurk = new Plurk();
            plurk.loadFromCursor(cursor);

            PlurkViewHolder holder = (PlurkViewHolder) view.getTag();
            holder.bind(cursor.getPosition(), plurk);
        }
    }
}
