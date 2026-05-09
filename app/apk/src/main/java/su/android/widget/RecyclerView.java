package su.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    private Paint scrollBarPaint;
    private RectF scrollBarRect;
    private float density;
    private int scrollBarColor = 0x33000000;

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
        density = context.getResources().getDisplayMetrics().density;
        setBackgroundColor(Color.WHITE);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        setVerticalScrollBarEnabled(false);
        
        scrollBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrollBarPaint.setColor(scrollBarColor);
        scrollBarRect = new RectF();

        addItemDecoration(new ItemDecoration(context));

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                applyStyle(view);
                applyRippleEffect(view);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {}
        });
    }

    private void applyStyle(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyStyle(group.getChildAt(i));
            }
        }
    }

    private void applyRippleEffect(View view) {
        TypedValue outValue = new TypedValue();
        view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        view.setBackgroundResource(outValue.resourceId);
        view.setClickable(true);
        view.setFocusable(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawIosScrollBar(canvas);
    }

    private void drawIosScrollBar(Canvas canvas) {
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int offset = computeVerticalScrollOffset();

        if (range <= extent) return;

        float width = getWidth();
        float height = getHeight();
        float thickness = 6 * density;
        float margin = 4 * density;

        float scrollBarHeight = (height * extent) / range;
        float scrollBarTop = (height * offset) / range;

        scrollBarRect.set(
            width - thickness - margin,
            scrollBarTop + margin,
            width - margin,
            scrollBarTop + scrollBarHeight - margin
        );

        canvas.drawRoundRect(scrollBarRect, thickness, thickness, scrollBarPaint);
    }

    public static class ItemDecoration extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
        private final Paint paint;

        public ItemDecoration(Context context) {
            paint = new Paint();
            paint.setColor(0x1F000000); 
            paint.setStrokeWidth(0.6f * context.getResources().getDisplayMetrics().density);
        }

        @Override
        public void onDrawOver(Canvas c, androidx.recyclerview.widget.RecyclerView parent, State state) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);
                float bottom = child.getBottom();
                c.drawLine(0, bottom, parent.getWidth(), bottom, paint);
            }
        }
    }
}