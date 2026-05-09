package su.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    private Paint scrollBarPaint;
    private RectF scrollBarRect;
    private float density;
    private static final int PURE_WHITE = 0xFFFFFFFF;
    private int scrollBarColor = 0x33000000;

    public RecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        setBackgroundColor(PURE_WHITE);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        setVerticalScrollBarEnabled(false);
        
        scrollBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrollBarPaint.setColor(scrollBarColor);
        scrollBarRect = new RectF();

        addItemDecoration(new ItemDecoration(context));

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                applyStyle(view);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {}
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
        float thickness = 4 * density;
        float margin = 3 * density;

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
        private final float margin;

        public ItemDecoration(Context context) {
            paint = new Paint();
            paint.setColor(0x1F000000); 
            paint.setStrokeWidth(0.6f * context.getResources().getDisplayMetrics().density);
            margin = 16 * context.getResources().getDisplayMetrics().density;
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView parent, @NonNull State state) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);
                float bottom = child.getBottom();
                c.drawLine(margin, bottom, parent.getWidth(), bottom, paint);
            }
        }
    }
}