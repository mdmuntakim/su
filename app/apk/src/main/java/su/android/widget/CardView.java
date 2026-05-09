package su.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

public class CardView extends MaterialCardView {

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        setCardBackgroundColor(isDark ? Color.BLACK : Color.WHITE);
        setStrokeWidth(0);
        setCardElevation(1f);
        setRippleColorResource(android.R.color.transparent);

        float density = getContext().getResources().getDisplayMetrics().density;
        float radius = 24f * density;

        setShapeAppearanceModel(
            getShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(radius)
                .build()
        );
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateChildStyle(child);
    }

    private void updateChildStyle(View view) {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
        int themeColor = isDark ? Color.WHITE : Color.BLACK;
        Typeface tf = Typeface.create("sans-serif-medium", Typeface.NORMAL);

        Drawable background = view.getBackground();
        if (background != null) {
            background.mutate().setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
        }

        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(themeColor);
            tv.setTypeface(tf);
            Drawable[] drawables = tv.getCompoundDrawables();
            for (Drawable d : drawables) {
                if (d != null) {
                    d.mutate().setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
                }
            }
        } else if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            iv.setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                updateChildStyle(group.getChildAt(i));
            }
        }
    }
}