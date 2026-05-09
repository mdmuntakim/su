package su.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private static final int LIGHT_BG_COLOR = 0xFFF2F2F7;
    private int scrollBarColor = 0x33000000;
    private static final float IOS_CORNER_RADIUS_DP = 10;
    private static final float IOS_GROUP_DIVIDER_ALPHA_INT = 31;
    private final float cornerRadius;

    public RecyclerView(@NonNull Context context) {
        super(context);
        cornerRadius = IOS_CORNER_RADIUS_DP * context.getResources().getDisplayMetrics().density;
        init(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cornerRadius = IOS_CORNER_RADIUS_DP * context.getResources().getDisplayMetrics().density;
        init(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cornerRadius = IOS_CORNER_RADIUS_DP * context.getResources().getDisplayMetrics().density;
        init(context);
    }

    private void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        setBackgroundColor(LIGHT_BG_COLOR);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        setVerticalScrollBarEnabled(false);
        setClipToPadding(false);
        int padding = (int) (16 * density);
        setPadding(padding, 0, padding, padding);
        
        scrollBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrollBarPaint.setColor(scrollBarColor);
        scrollBarRect = new RectF();

        addItemDecoration(new GroupedItemDecoration(context, cornerRadius));

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
            ((TextView) view).setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            ((TextView) view).setTextColor(0xFF000000);
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

    public static class GroupedItemDecoration extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
        private final Paint dividerPaint;
        private final Paint backgroundPaint;
        private final float dividerMargin;
        private final float cornerRadius;
        private final Path path = new Path();
        private final RectF rect = new RectF();

        public GroupedItemDecoration(Context context, float cornerRadius) {
            this.cornerRadius = cornerRadius;
            dividerPaint = new Paint();
            dividerPaint.setColor(Color.argb((int) IOS_GROUP_DIVIDER_ALPHA_INT, 0, 0, 0));
            dividerPaint.setStrokeWidth(0.6f * context.getResources().getDisplayMetrics().density);
            
            backgroundPaint = new Paint();
            backgroundPaint.setColor(0xFFFFFFFF);
            backgroundPaint.setStyle(Paint.Style.FILL);
            
            dividerMargin = 16 * context.getResources().getDisplayMetrics().density;
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView parent, @NonNull State state) {
            final int childCount = parent.getChildCount();
            if (childCount == 0) return;

            int firstGroupChildIndex = -1;
            int lastGroupChildIndex = -1;

            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final ViewHolder viewHolder = parent.getChildViewHolder(child);
                
                int viewType = viewHolder.getItemViewType();
                if (viewType != 1 && viewType != 2 && viewType != 3) {
                    drawGroupBackground(c, parent, firstGroupChildIndex, lastGroupChildIndex);
                    firstGroupChildIndex = -1;
                    lastGroupChildIndex = -1;
                    continue;
                }

                if (firstGroupChildIndex == -1) {
                    firstGroupChildIndex = i;
                }
                lastGroupChildIndex = i;
            }
            drawGroupBackground(c, parent, firstGroupChildIndex, lastGroupChildIndex);
        }

        private void drawGroupBackground(Canvas c, androidx.recyclerview.widget.RecyclerView parent, int firstIdx, int lastIdx) {
            if (firstIdx == -1 || lastIdx == -1) return;

            final View firstChild = parent.getChildAt(firstIdx);
            final View lastChild = parent.getChildAt(lastIdx);

            path.reset();
            rect.set(parent.getPaddingLeft(), firstChild.getTop(), parent.getWidth() - parent.getPaddingRight(), lastChild.getBottom());
            path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
            c.drawPath(path, backgroundPaint);
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView parent, @NonNull State state) {
            final int childCount = parent.getChildCount();
            if (childCount <= 1) return;

            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final ViewHolder viewHolder = parent.getChildViewHolder(child);
                final View nextChild = parent.getChildAt(i + 1);
                final ViewHolder nextViewHolder = parent.getChildViewHolder(nextChild);

                int viewType = viewHolder.getItemViewType();
                int nextViewType = nextViewHolder.getItemViewType();

                if ((viewType == 1 || viewType == 2 || viewType == 3) && 
                    (nextViewType == 1 || nextViewType == 2 || nextViewType == 3)) {
                    final float bottom = child.getBottom();
                    c.drawLine(parent.getPaddingLeft() + dividerMargin, bottom, parent.getWidth() - parent.getPaddingRight(), bottom, dividerPaint);
                }
            }
        }
    }
}