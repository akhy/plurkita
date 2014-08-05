package net.akhyar.plurkita;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.akhyar.plurkita.model.User;
import net.akhyar.plurkita.util.CircleTransform;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author akhyar
 */
public class UserActivity extends BaseActivity implements AuthFragment.Listener {


    private static final int REQ_VERIFIER = 1001;

    @Inject
    Session session;
    @InjectView(R.id.avatar)
    ImageView avatar;
    @InjectView(R.id.nickName)
    TextView nickName;

    private AuthFragment authFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.inject(this);
        Application.inject(this);

        authFragment = (AuthFragment) getSupportFragmentManager().findFragmentById(R.id.authFragment);
    }

    @Override
    public void requestVerifier(String queryParams) {
        Intent verifierIntent = AuthorizeActivity.createIntent(this, queryParams);
        startActivityForResult(verifierIntent, REQ_VERIFIER);
    }

    @Override
    public void onSessionInvalidated() {
        User user = session.getUser();
        if (user == null) {
            getSupportActionBar().setTitle("Log in to Plurk"); // TODO move to strings.xml
            avatar.setImageResource(R.drawable.ic_launcher);
            nickName.setText("");
        } else {
            getSupportActionBar().setTitle(user.getDisplayName());
            Picasso.with(this).load(user.getAvatarUrl())
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .resize(R.dimen.avatar_big_size, R.dimen.avatar_big_size)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(avatar);
            nickName.setText(user.getNickName());
        }
    }

    @Override
    public void onAuthSuccess() {
        startActivity(new Intent(this, TimelineActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_VERIFIER) {
            if (resultCode == RESULT_OK) {
                String verifier = data.getStringExtra(AuthorizeActivity.EXTRA_OAUTH_VERIFIER);
                authFragment.requestAccessToken(verifier);
            } else {
                authFragment.onError(new Exception("Failed to authorize"));
            }
        }
    }
}
