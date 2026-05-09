package su.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

public class Slider extends com.google.android.material.slider.Slider {

    public Slider(Context context) {
        super(context);
        init();
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        int mainColor = isDark ? Color.WHITE : Color.BLACK;
        int trackAlphaColor = isDark ? Color.argb(77, 255, 255, 255) : Color.argb(77, 0, 0, 0);

        setThumbColor(ColorStateList.valueOf(mainColor));
        setTrackActiveColor(ColorStateList.valueOf(mainColor));
        setTrackInactiveColor(ColorStateList.valueOf(trackAlphaColor));
        setTickVisible(false);
        setHaloRadius(0);

        float density = getContext().getResources().getDisplayMetrics().density;
        float radius = 24f * density;

        com.google.android.material.shape.ShapeAppearanceModel shapeModel = 
            new com.google.android.material.shape.ShapeAppearanceModel.Builder()
                .setAllCornerSizes(radius)
                .build();
                
        setShapeAppearanceModel(shapeModel);
    }
}