package su.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardView extends LinearLayout {

    private int cardBackgroundColor = 0xFFFFFFFF;
    private int cornerRadius;
    private int cardPadding;
    
    private LinearLayout contentLayout;
    private TextView titleView;
    private TextView subtitleView;
    private ImageView iconView;
    private ImageView chevronView;
    
    private float density;

    public CardView(Context context) {
        super(context);
        init(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        density = context.getResources().getDisplayMetrics().density;
        
        cornerRadius = (int) (12 * density);
        cardPadding = (int) (16 * density);
        
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setClickable(true);
        setFocusable(true);
        
        updateBackground();
        setPadding(cardPadding, (int) (12 * density), cardPadding, (int) (12 * density));

        iconView = new ImageView(context);
        LayoutParams iconParams = new LayoutParams((int) (28 * density), (int) (28 * density));
        iconParams.marginEnd = (int) (16 * density);
        iconView.setLayoutParams(iconParams);
        iconView.setVisibility(GONE);
        addView(iconView);

        contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(VERTICAL);
        LayoutParams contentParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        contentLayout.setLayoutParams(contentParams);

        titleView = new TextView(context);
        titleView.setTextSize(17);
        titleView.setTextColor(0xFF000000);
        titleView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        contentLayout.addView(titleView);

        subtitleView = new TextView(context);
        subtitleView.setTextSize(14);
        subtitleView.setTextColor(0xFF8E8E93);
        subtitleView.setVisibility(GONE);
        subtitleView.setPadding(0, (int) (2 * density), 0, 0);
        contentLayout.addView(subtitleView);

        addView(contentLayout);

        chevronView = new ImageView(context);
        LayoutParams chevronParams = new LayoutParams((int) (14 * density), (int) (14 * density));
        chevronView.setLayoutParams(chevronParams);
        chevronView.setColorFilter(0xFFC7C7CC);
        chevronView.setVisibility(VISIBLE);
        addView(chevronView);
    }

    private void updateBackground() {
        float[] outerRadii = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        RoundRectShape shape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.getPaint().setColor(cardBackgroundColor);

        RippleDrawable rippleDrawable = new RippleDrawable(
                ColorStateList.valueOf(Color.parseColor("#1F000000")),
                shapeDrawable,
                shapeDrawable
        );
        setBackground(rippleDrawable);
    }

    public void setTitle(String text) {
        titleView.setText(text);
    }

    public void setSubtitle(String text) {
        if (text == null || text.isEmpty()) {
            subtitleView.setVisibility(GONE);
        } else {
            subtitleView.setVisibility(VISIBLE);
            subtitleView.setText(text);
        }
    }

    public void setIcon(int resId) {
        if (resId == 0) {
            iconView.setVisibility(GONE);
        } else {
            iconView.setVisibility(VISIBLE);
            iconView.setImageResource(resId);
        }
    }

    public void setIconBackgroundColor(int color) {
        float[] radii = new float[]{8 * density, 8 * density, 8 * density, 8 * density, 8 * density, 8 * density, 8 * density, 8 * density};
        ShapeDrawable bg = new ShapeDrawable(new RoundRectShape(radii, null, null));
        bg.getPaint().setColor(color);
        iconView.setBackground(bg);
        int p = (int) (5 * density);
        iconView.setPadding(p, p, p, p);
    }

    public void setCardBackgroundColor(int color) {
        this.cardBackgroundColor = color;
        updateBackground();
    }

    public void setCornerRadius(int radiusDp) {
        this.cornerRadius = (int) (radiusDp * density);
        updateBackground();
    }

    public void showChevron(boolean show) {
        chevronView.setVisibility(show ? VISIBLE : GONE);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getSubtitleView() {
        return subtitleView;
    }

    public ImageView getIconView() {
        return iconView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}