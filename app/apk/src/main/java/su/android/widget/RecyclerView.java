package su.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    private Paint scrollBarPaint;
    private RectF scrollBarRect;
    private float density;
    private int iosBgColor = 0xFFF2F2F7;
    private int scrollBarColor = 0x4D000000;

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
        setBackgroundColor(iosBgColor);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        setVerticalScrollBarEnabled(false);
        
        scrollBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrollBarPaint.setColor(scrollBarColor);
        scrollBarRect = new RectF();
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
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
        float thickness = 3 * density;
        float margin = 4 * density;

        float scrollBarHeight = (height * extent) / range;
        float scrollBarTop = (height * offset) / range;

        scrollBarRect.set(
            width - thickness - margin,
            scrollBarTop + margin,
            width - margin,
            scrollBarTop + scrollBarHeight - margin
        );

        canvas.drawRoundRect(scrollBarRect, thickness / 2, thickness / 2, scrollBarPaint);
    }

    public void setIosBackground(int color) {
        this.iosBgColor = color;
        setBackgroundColor(iosBgColor);
    }

    public static class ItemDecoration extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
        private Paint paint;
        private float margin;

        public ItemDecoration(Context context) {
            paint = new Paint();
            paint.setColor(0x4D3C3C43);
            paint.setStrokeWidth(0.5f * context.getResources().getDisplayMetrics().density);
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