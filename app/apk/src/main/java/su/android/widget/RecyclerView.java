package su.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.content.res.Configuration;
import android.graphics.Color;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    public RecyclerView(Context context) {
        super(context);
        init(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        applyThemeBackground(context);
    }

    private void applyThemeBackground(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            setBackgroundColor(Color.parseColor("#1A1A1A"));
        } else {
            setBackgroundColor(Color.parseColor("#F0F0F0"));
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        applyThemeBackground(getContext());
    }
}