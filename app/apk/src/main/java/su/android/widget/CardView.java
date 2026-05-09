package su.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardView extends LinearLayout {

    private int cardBackgroundColor = 0xFFFFFFFF;
    private int cornerRadius;
    
    private LinearLayout mainContentLayout;
    private TextView titleView;
    private TextView descriptionView;
    private FrameLayout customActionContainer;
    private View separator;
    private LinearLayout buttonContainer;
    
    private float density;

    public CardView(Context context) {
        super(context);
        init(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        cornerRadius = (int) (14 * density);
        
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        updateBackground();

        mainContentLayout = new LinearLayout(context);
        mainContentLayout.setOrientation(VERTICAL);
        mainContentLayout.setPadding((int) (20 * density), (int) (20 * density), (int) (20 * density), (int) (20 * density));
        mainContentLayout.setGravity(Gravity.CENTER);
        
        titleView = new TextView(context);
        titleView.setTextSize(18);
        titleView.setTextColor(0xFF000000);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        mainContentLayout.addView(titleView);

        descriptionView = new TextView(context);
        descriptionView.setTextSize(14);
        descriptionView.setTextColor(0xFF000000);
        descriptionView.setGravity(Gravity.CENTER);
        descriptionView.setPadding(0, (int) (8 * density), 0, 0);
        mainContentLayout.addView(descriptionView);

        customActionContainer = new FrameLayout(context);
        mainContentLayout.addView(customActionContainer);
        
        addView(mainContentLayout);

        separator = new View(context);
        separator.setBackgroundColor(0xFFE0E0E0);
        addView(separator, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (1 * density)));

        buttonContainer = new LinearLayout(context);
        buttonContainer.setOrientation(HORIZONTAL);
        buttonContainer.setDividerDrawable(new ShapeDrawable(new RoundRectShape(new float[]{0,0,0,0,0,0,0,0}, null, null)));
        buttonContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        addView(buttonContainer, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (50 * density)));
    }

    private void updateBackground() {
        float[] radii = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(radii, null, null));
        shape.getPaint().setColor(cardBackgroundColor);
        setBackground(shape);
    }

    public void setTitle(String text) {
        titleView.setText(text);
    }

    public void setDescription(String text) {
        descriptionView.setText(text);
    }

    public void addActionView(View view) {
        customActionContainer.removeAllViews();
        if (view != null) {
            customActionContainer.setPadding(0, (int) (16 * density), 0, 0);
            customActionContainer.addView(view);
        }
    }

    public void addButton(String text, int textColor, OnClickListener listener) {
        if (buttonContainer.getChildCount() > 0) {
            View vLine = new View(getContext());
            vLine.setBackgroundColor(0xFFE0E0E0);
            buttonContainer.addView(vLine, new LayoutParams((int) (1 * density), LayoutParams.MATCH_PARENT));
        }

        TextView btn = new TextView(getContext());
        btn.setText(text);
        btn.setTextColor(textColor);
        btn.setTextSize(17);
        btn.setGravity(Gravity.CENTER);
        btn.setClickable(true);
        btn.setFocusable(true);
        btn.setOnClickListener(listener);
        
        float[] radii = new float[]{0,0,0,0,cornerRadius,cornerRadius,cornerRadius,cornerRadius};
        ShapeDrawable mask = new ShapeDrawable(new RoundRectShape(radii, null, null));
        RippleDrawable ripple = new RippleDrawable(ColorStateList.valueOf(0x1F000000), null, mask);
        btn.setBackground(ripple);

        buttonContainer.addView(btn, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
    }

    public void setCardBackgroundColor(int color) {
        this.cardBackgroundColor = color;
        updateBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = (int) (300 * density);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSize > maxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}