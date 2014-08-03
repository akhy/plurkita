package net.akhyar.plurkita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import net.akhyar.android.adapters.ViewHolderListAdapter;
import net.akhyar.android.helpers.ViewUtil;
import net.akhyar.plurkita.api.ErrorEvent;
import net.akhyar.plurkita.api.ResponseApi;
import net.akhyar.plurkita.event.ResponseAdded;
import net.akhyar.plurkita.event.ResponsesLoaded;
import net.akhyar.plurkita.model.Conversation;
import net.akhyar.plurkita.model.Plurk;
import net.akhyar.plurkita.model.PlurkViewHolder;
import net.akhyar.plurkita.model.Response;
import net.akhyar.plurkita.model.ResponseViewHolder;
import net.akhyar.plurkita.model.User;
import net.akhyar.plurkita.util.IntentLauncher;

import java.util.List;

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

/**
 * @author akhyar
 */
public class ResponsesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.list)
    ListView list;
    @InjectView(R.id.send)
    Button send;
    @InjectView(R.id.content)
    EditText content;
    @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.emptyView)
    View emptyView;
    @Inject
    ResponseApi responseApi;
    @Inject
    EventBus eventBus;

    private ResponseAdapter mAdapter = new ResponseAdapter();
    private Plurk mPlurk;
    private Action1<Throwable> errorHandler = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            errorBus.post(new ErrorEvent(throwable));
        }
    };

    public static IntentLauncher create(Context context, long id) {
        Intent intent = new Intent(context, ResponsesActivity.class);
        intent.putExtra(Plurk.ID, id);
        return new IntentLauncher(context, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses);
        Application.inject(this);
        ButterKnife.inject(this);
        eventBus.register(this);

        mPlurk = Plurk.find(getPlurkId());

        PlurkViewHolder holder = new PlurkViewHolder();
        View header = holder.createView(this, null);
        holder.bind(0, mPlurk);

        list.addHeaderView(header);
        list.setAdapter(mAdapter);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorScheme(
                R.color.tertiary_lightest,
                R.color.complementary_lighter,
                R.color.tertiary_lighter,
                R.color.complementary_lightest
        );
        swipeContainer.setRefreshing(true);
        onRefresh();
    }

    private long getPlurkId() {
        return getIntent().getLongExtra(Plurk.ID, 0);
    }

    @Override
    public void onRefresh() {
        responseApi.getConversation(getPlurkId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Conversation, Observable<List<Response>>>() {
                    @DebugLog
                    private void saveUsersFromConversation(Iterable<User> users) {
                        ActiveAndroid.beginTransaction();
                        for (User user : users) {
                            User.upsert(user);
                        }
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
                    }

                    @Override
                    public Observable<List<Response>> call(Conversation conversation) {
                        saveUsersFromConversation(conversation.getUsers().values());
                        return Observable.just(conversation.getResponses());
                    }
                })
                .subscribe(new Action1<List<Response>>() {
                    @Override
                    public void call(List<Response> responses) {
                        eventBus.post(new ResponsesLoaded(getPlurkId(), responses));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeContainer.setRefreshing(false);
                        errorHandler.call(throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        swipeContainer.setRefreshing(false);
                    }
                });
    }

    @OnClick(R.id.send)
    public void onSend() {
        send.setEnabled(false);
        content.setEnabled(false);
        String contentText = this.content.getText().toString().trim();
        responseApi.postResponse(getPlurkId(), contentText, "freestyle")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        eventBus.post(new ResponseAdded(getPlurkId(), response));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        send.setEnabled(true);
                        content.setEnabled(true);
                        errorHandler.call(throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        send.setEnabled(true);
                        content.setEnabled(true);
                        content.setText("");
                    }
                });
    }

    @OnItemClick(R.id.list)
    public void onMention(int pos) {
        pos -= list.getHeaderViewsCount(); // getting actual position
        long userId = pos >= 0
                ? mAdapter.getItem(pos).getUserId() // userId of clicked response
                : mPlurk.getOwnerId(); // userId of currently viewed plurk's owner

        User user = User.find(userId);
        String newText = String.format("@%s: %s", user.getNickName(), content.getText());
        content.setText(newText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    public void onEventMainThread(ResponsesLoaded event) {
        if (event.getPlurkId() == getPlurkId()) {
            ViewUtil.setPresence(emptyView, event.getResponses().isEmpty());
            mAdapter.setList(event.getResponses());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(ResponseAdded event) {
        if (event.getPlurkId() == getPlurkId()) {
            ViewUtil.setPresence(emptyView, false);
            mAdapter.getList().add(event.getResponse());
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ResponseAdapter extends ViewHolderListAdapter<Response> {

        @Override
        public ViewHolder<Response> createHolder() {
            return new ResponseViewHolder();
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}
