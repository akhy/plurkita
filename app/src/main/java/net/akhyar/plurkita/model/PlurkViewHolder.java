package net.akhyar.plurkita.model;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.akhyar.android.adapters.ViewHolderListAdapter;
import net.akhyar.android.helpers.ViewUtil;
import net.akhyar.plurkita.R;
import net.akhyar.plurkita.util.CircleTransform;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author akhyar
 */
public class PlurkViewHolder extends ViewHolderListAdapter.ViewHolder<Plurk> {

    Context context;

    @InjectView(R.id.contentText)
    TextView content;
    @InjectView(R.id.displayName)
    TextView displayName;
    @InjectView(R.id.nickName)
    TextView nickName;
    @InjectView(R.id.responseCount)
    TextView responseCount;
    @InjectView(R.id.favoriteCount)
    TextView favoriteCount;
    @InjectView(R.id.replurkCount)
    TextView replurkCount;
    @InjectView(R.id.avatar)
    ImageView avatar;

    @Override
    public View createView(Context context, ViewGroup parent) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_plurk, null);
        ButterKnife.inject(this, view);
        view.setTag(this);
        return view;
    }

    @Override
    public void bind(int pos, Plurk plurk) {
        User user = User.find(plurk.getOwnerId());
        if (user != null) {
            displayName.setText(user.getDisplayName());
            nickName.setText(String.format("@%s", user.getNickName()));

            Picasso.with(context)
                    .load(user.getAvatarUrl())
                    .resizeDimen(R.dimen.avatar_size, R.dimen.avatar_size)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(avatar);
        }
        content.setText(Html.fromHtml(plurk.getContent()));
        responseCount.setText(context.getString(R.string.response_count, plurk.getResponseCount()));
        favoriteCount.setText(context.getString(R.string.favorite_count, plurk.getFavoriteCount()));
        replurkCount.setText(context.getString(R.string.replurk_count, plurk.getReplurkersCount()));

        ViewUtil.setPresence(replurkCount, plurk.getReplurkersCount() > 0);
        ViewUtil.setPresence(responseCount, plurk.getNoComments() != 1);
    }
}
