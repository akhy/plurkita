package net.akhyar.android.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import net.akhyar.plurkita.R;

/**
 * @author akhyar
 */
public class FloatingActionButton extends View {

    final static OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    final static AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private final static int DEFAULT_BACKGROUND_COLOR = R.color.white;
    private static final int DEFAULT_DRAWABLE = R.drawable.ic_action_new;
    Context context;
    Paint mButtonPaint;
    Paint mDrawablePaint;
    Bitmap mBitmap;
    boolean mHidden = false;

    public FloatingActionButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setStyle(Paint.Style.FILL);
        changePressedState(false);
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (attrs == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), DEFAULT_DRAWABLE);
            mButtonPaint.setColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR));
        } else {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton);
            int drawableRes = ta.getResourceId(R.styleable.FloatingActionButton_fab_drawable, DEFAULT_DRAWABLE);
            mBitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
            int backgroundRes = ta.getResourceId(R.styleable.FloatingActionButton_fab_background_color, DEFAULT_BACKGROUND_COLOR);
            mButtonPaint.setColor(getResources().getColor(backgroundRes));
        }

        invalidate();
    }

    private void changePressedState(boolean pressed) {
        if (pressed) {
            setTranslationY(-3f);
            setAlpha(1f);
            mButtonPaint.setShadowLayer(10.0f, 0.0f, 4f, Color.argb(100, 0, 0, 0));
        } else {
            setTranslationY(0f);
            setAlpha(0.9f);
            mButtonPaint.setShadowLayer(5.0f, 0.0f, 3f, Color.argb(100, 0, 0, 0));
        }
    }


    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setFloatingActionButtonColor(int FloatingActionButtonColor) {
    }

    public void setFloatingActionButtonDrawable(Drawable drawable) {
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            changePressedState(false);
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            changePressedState(true);
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setClickable(true);
        if (mBitmap != null) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                    (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }
    }

    public void hideFloatingActionButton() {
        if (!mHidden) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(accelerateInterpolator);
            animSetXY.setDuration(100);
            animSetXY.start();
            mHidden = true;
        }
    }

    public void showFloatingActionButton() {
        if (mHidden) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(overshootInterpolator);
            animSetXY.setDuration(200);
            animSetXY.start();
            mHidden = false;
        }
    }

    public boolean isHidden() {
        return mHidden;
    }

    static public class Builder {
        private final Activity activity;
        int gravity = Gravity.BOTTOM | Gravity.RIGHT; // default bottom right
        Drawable drawable;
        int color = Color.WHITE;
        int size = 0;
        float scale = 0;
        private FrameLayout.LayoutParams params;

        public Builder(Activity context) {
            scale = context.getResources().getDisplayMetrics().density;
            size = convertToPixels(72, scale); // default size is 72dp by 72dp
            params = new FrameLayout.LayoutParams(size, size);
            params.gravity = gravity;

            this.activity = context;
        }

        // The calculation (value * scale + 0.5f) is a widely used to convert to dps to pixel units
        // based on density scale
        // see developer.android.com (Supporting Multiple Screen Sizes)
        private int convertToPixels(int dp, float scale) {
            return (int) (dp * scale + 0.5f);
        }

        /**
         * Sets the gravity for the FAB
         */
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the margins for the FAB in dp
         */
        public Builder withMargins(int left, int top, int right, int bottom) {
            params.setMargins(
                    convertToPixels(left, scale),
                    convertToPixels(top, scale),
                    convertToPixels(right, scale),
                    convertToPixels(bottom, scale));
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public Builder withDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * Sets the FAB color
         */
        public Builder withButtonColor(final int color) {
            this.color = color;
            return this;
        }

        /**
         * Sets the FAB size in dp
         */
        public Builder withButtonSize(int size) {
            size = convertToPixels(size, scale);
            params = new FrameLayout.LayoutParams(size, size);
            return this;
        }

        public FloatingActionButton create() {
            final FloatingActionButton button = new FloatingActionButton(activity);
            button.setFloatingActionButtonColor(this.color);
            button.setFloatingActionButtonDrawable(this.drawable);
            params.gravity = this.gravity;
            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            root.addView(button, params);
            return button;
        }
    }
}