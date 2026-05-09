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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardView extends LinearLayout {

    private int cardBackgroundColor = 0xF2FFFFFF;
    private int cornerRadius;
    private int dividerColor = 0x4D3C3C43;
    private int accentColor = 0xFF007AFF;
    
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
        mainContentLayout.setPadding((int) (16 * density), (int) (20 * density), (int) (16 * density), (int) (20 * density));
        mainContentLayout.setGravity(Gravity.CENTER);
        
        titleView = new TextView(context);
        titleView.setTextSize(17);
        titleView.setTextColor(0xFF000000);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        mainContentLayout.addView(titleView);

        descriptionView = new TextView(context);
        descriptionView.setTextSize(13);
        descriptionView.setTextColor(0xFF000000);
        descriptionView.setGravity(Gravity.CENTER);
        descriptionView.setPadding(0, (int) (4 * density), 0, 0);
        mainContentLayout.addView(descriptionView);

        customActionContainer = new FrameLayout(context);
        mainContentLayout.addView(customActionContainer);
        
        addView(mainContentLayout);

        separator = new View(context);
        separator.setBackgroundColor(dividerColor);
        addView(separator, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (0.5f * density)));

        buttonContainer = new LinearLayout(context);
        buttonContainer.setOrientation(HORIZONTAL);
        addView(buttonContainer, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (44 * density)));
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
            customActionContainer.setPadding(0, (int) (12 * density), 0, 0);
            customActionContainer.addView(view);
        }
    }

    public void addButton(String text, boolean isBold, OnClickListener listener) {
        if (buttonContainer.getChildCount() > 0) {
            View vLine = new View(getContext());
            vLine.setBackgroundColor(dividerColor);
            buttonContainer.addView(vLine, new LayoutParams((int) (0.5f * density), LayoutParams.MATCH_PARENT));
        }

        TextView btn = new TextView(getContext());
        btn.setText(text);
        btn.setTextColor(accentColor);
        btn.setTextSize(17);
        btn.setGravity(Gravity.CENTER);
        btn.setClickable(true);
        btn.setFocusable(true);
        btn.setOnClickListener(listener);
        
        if (isBold) {
            btn.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        } else {
            btn.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        }
        
        float[] radii = new float[]{0, 0, 0, 0, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        ShapeDrawable mask = new ShapeDrawable(new RoundRectShape(radii, null, null));
        RippleDrawable ripple = new RippleDrawable(ColorStateList.valueOf(0x1A000000), null, mask);
        btn.setBackground(ripple);

        buttonContainer.addView(btn, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
    }

    public void setCardBackgroundColor(int color) {
        this.cardBackgroundColor = color;
        updateBackground();
    }

    public void setAccentColor(int color) {
        this.accentColor = color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = (int) (270 * density);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSize > maxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}