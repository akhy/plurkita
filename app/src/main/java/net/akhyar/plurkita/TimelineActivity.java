package net.akhyar.plurkita;

import android.os.Bundle;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import net.akhyar.android.adapters.ViewHolderListAdapter;
import net.akhyar.plurkita.api.TimelineApi;
import net.akhyar.plurkita.model.Plurk;
import net.akhyar.plurkita.model.PlurkViewHolder;
import net.akhyar.plurkita.model.Timeline;
import net.akhyar.plurkita.model.User;

import java.util.Iterator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class TimelineActivity extends BaseActivity {

    @Inject
    Timber.Tree log;
    @InjectView(R.id.list)
    ListView list;

    PlurkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Application.inject(this);
        ButterKnife.inject(this);

        adapter = new PlurkAdapter();
        list.setAdapter(adapter);
    }

    @OnClick(R.id.getPlurks)
    public void getPlurks() {
        Application.getInstance(TimelineApi.class)
                .getPlurksObservable()
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
                            User existing = User.find(user);
                            if (existing == null) {
                                log.d("Saving new user: %s", user.getNickName());
                                user.save();
                            } else {
                                log.d("Updating user: %s", user.getNickName());
                                existing.copyFrom(user);
                                existing.save();
                            }
                        }
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
                        return timeline;
                    }
                })
                .subscribe(new Action1<Timeline>() {
                    @Override
                    public void call(Timeline timeline) {
                        adapter.setList(timeline.getPlurks());
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        log.e(throwable, "Error refreshing timeline");
                    }
                });
    }

    private class PlurkAdapter extends ViewHolderListAdapter<Plurk> {

        @Override
        public ViewHolder<Plurk> createHolder() {
            return new PlurkViewHolder();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

}
