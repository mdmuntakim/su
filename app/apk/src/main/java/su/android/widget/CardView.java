package su.android.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.ShapeAppearanceModel;

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
        setCardBackgroundColor(Color.WHITE);
        setStrokeWidth(0);
        setCardElevation(0f);
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
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        applyBlackText(child);
    }

    private void applyBlackText(android.view.View view) {
        if (view instanceof android.widget.TextView) {
            ((android.widget.TextView) view).setTextColor(Color.BLACK);
        } else if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyBlackText(group.getChildAt(i));
            }
        }
    }
}