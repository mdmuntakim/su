package su.android.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

public class Switch extends android.widget.Switch {

    private int trackColorOn = 0xFF2196F3;
    private int trackColorOff = 0xFFD1D1D1;
    private int thumbColor = 0xFFFFFFFF;

    private float progress = 0f;
    private float stretch = 0f;
    private ValueAnimator animator;

    private Paint paint;
    private RectF trackRect;
    private RectF thumbRect;

    private float touchX;
    private float startX;
    private boolean isDragging;
    private int touchSlop;

    public Switch(Context context) {
        super(context);
        init(context);
    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackRect = new RectF();
        thumbRect = new RectF();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
        setFocusable(true);
        setBackground(null);
        progress = isChecked() ? 1.0f : 0.0f;
    }

    public void setTrackColorOn(int color) {
        this.trackColorOn = color;
        invalidate();
    }

    public void setTrackColors(int onColor, int offColor) {
        this.trackColorOn = onColor;
        this.trackColorOff = offColor;
        invalidate();
    }

    public void setThumbColor(int color) {
        this.thumbColor = color;
        invalidate();
    }

    @Override
    public void setChecked(boolean checked) {
        boolean changed = isChecked() != checked;
        super.setChecked(checked);
        if (changed) {
            if (isAttachedToWindow() && getVisibility() == VISIBLE) {
                animateToState(checked);
            } else {
                if (animator != null) animator.cancel();
                progress = checked ? 1.0f : 0.0f;
                stretch = 0f;
                invalidate();
            }
        }
    }

    private void animateToState(boolean checked) {
        if (animator != null) animator.cancel();
        animator = ValueAnimator.ofFloat(progress, checked ? 1.0f : 0.0f);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            stretch = (float) Math.sin(progress * Math.PI) * 0.12f;
            invalidate();
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                touchX = x;
                isDragging = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - touchX;
                if (!isDragging && Math.abs(x - startX) > touchSlop) {
                    isDragging = true;
                    if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (isDragging) {
                    float range = getWidth() - getHeight();
                    float newProgress = progress + (deltaX / range);
                    progress = Math.max(0f, Math.min(1f, newProgress));
                    stretch = 0.05f;
                    touchX = x;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    setChecked(progress > 0.5f);
                    animateToState(isChecked());
                } else {
                    toggle();
                }
                isDragging = false;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float r = height / 2;
        float padding = height * 0.12f;

        trackRect.set(0, 0, width, height);
        paint.setColor(interpolateColor(trackColorOff, trackColorOn, progress));
        canvas.drawRoundRect(trackRect, r, r, paint);

        float sX = r;
        float eX = width - r;
        float currentThumbX = sX + (eX - sX) * progress;

        float thumbRadius = (r - padding) * 1.05f;
        float horizontalStretch = stretch * thumbRadius;
        float verticalShrink = stretch * thumbRadius * 0.4f;
        
        paint.setColor(thumbColor);
        paint.setShadowLayer(padding, 0, padding / 4, 0x14000000);
        
        thumbRect.set(
            currentThumbX - thumbRadius - horizontalStretch,
            r - thumbRadius + verticalShrink,
            currentThumbX + thumbRadius + horizontalStretch,
            r + thumbRadius - verticalShrink
        );
        
        canvas.drawRoundRect(thumbRect, thumbRadius, thumbRadius, paint);
        paint.clearShadowLayer();
    }

    private int interpolateColor(int color1, int color2, float fraction) {
        int a = 0xFF;
        int r = (int) ((color1 >> 16 & 0xff) + fraction * ((color2 >> 16 & 0xff) - (color1 >> 16 & 0xff)));
        int g = (int) ((color1 >> 8 & 0xff) + fraction * ((color2 >> 8 & 0xff) - (color1 >> 8 & 0xff)));
        int b = (int) ((color1 & 0xff) + fraction * ((color2 & 0xff) - (color1 & 0xff)));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float density = getResources().getDisplayMetrics().density;
        int width = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? 
                   MeasureSpec.getSize(widthMeasureSpec) : (int) (density * 48);
        int height = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ? 
                    MeasureSpec.getSize(heightMeasureSpec) : (int) (density * 26);
        setMeasuredDimension(width, height);
    }
}