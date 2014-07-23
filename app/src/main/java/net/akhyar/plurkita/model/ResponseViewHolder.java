package net.akhyar.plurkita.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.akhyar.android.adapters.ViewHolderListAdapter;
import net.akhyar.plurkita.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author akhyar
 */
public class ResponseViewHolder extends ViewHolderListAdapter.ViewHolder<Response> {

    Context context;

    @InjectView(R.id.contentText)
    TextView content;
    @InjectView(R.id.displayName)
    TextView displayName;
    @InjectView(R.id.nickName)
    TextView nickName;
    @InjectView(R.id.avatar)
    ImageView avatar;

    @Override
    public View createView(Context context, ViewGroup parent) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_response, null);
        ButterKnife.inject(this, view);
        view.setTag(this);
        return view;
    }

    @Override
    public void bind(int pos, Response response) {
        User user = User.find(response.getUserId());
        if (user != null) {
            displayName.setText(user.getDisplayName());
            nickName.setText(String.format("@%s", user.getNickName()));

            Picasso.with(context)
                    .load(user.getAvatarUrl()).into(avatar);
        }
        content.setText(response.getContentRaw());
    }
}
