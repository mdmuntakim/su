package su.android.widget;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Dialog {

    private Activity activity;
    private android.app.Dialog dialog;
    
    private String titleText = "Alert";
    private String messageText = "This is a default message.";
    private String leftBtnText = "";
    private String rightBtnText = "OK";
    
    private int cardWidth = 850;
    private float cardRadius = 60f;
    private boolean showLines = false;

    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;
    
    private List<View> customTopViews = new ArrayList<>();
    private List<View> customMiddleViews = new ArrayList<>();
    private List<View> customBottomViews = new ArrayList<>();

    public Dialog(Activity activity) {
        this.activity = activity;
    }

    public Dialog setTitle(String title) {
        this.titleText = title;
        return this;
    }

    public Dialog setMessage(String message) {
        this.messageText = message;
        return this;
    }

    public Dialog setLeftButton(String text, View.OnClickListener listener) {
        this.leftBtnText = text;
        this.leftListener = listener;
        return this;
    }

    public Dialog setRightButton(String text, View.OnClickListener listener) {
        this.rightBtnText = text;
        this.rightListener = listener;
        return this;
    }

    public void show() {
        boolean isDark = (activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        
        int bgColor = isDark ? 0xDD1C1C1E : 0xDDFFFFFF;
        int textColor = isDark ? Color.WHITE : Color.BLACK;
        
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.colorControlHighlight, typedValue, true);
        int rippleColor = typedValue.data;
        
        activity.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        int systemAccentColor = typedValue.data;

        dialog = new android.app.Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window dWin = dialog.getWindow();
        if (dWin != null) {
            dWin.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = dWin.getAttributes();
            lp.dimAmount = 0.4f;
            dWin.setAttributes(lp);
            if (Build.VERSION.SDK_INT >= 31) {
                dWin.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dWin.getAttributes().setBlurBehindRadius(35);
            }
        }

        LinearLayout root = new LinearLayout(activity);
        root.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        root.setGravity(Gravity.CENTER);

        final LinearLayout card = new LinearLayout(activity);
        card.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(bgColor);
        cardBg.setCornerRadius(cardRadius);
        card.setBackground(cardBg);
        card.setLayoutParams(new LinearLayout.LayoutParams(cardWidth, -2));

        LinearLayout textContainer = new LinearLayout(activity);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setPadding(60, 70, 60, 70);
        textContainer.setGravity(Gravity.CENTER);

        if (titleText != null && !titleText.isEmpty()) {
            TextView title = new TextView(activity);
            title.setText(titleText);
            title.setTextSize(18);
            title.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            title.setTextColor(textColor);
            title.setGravity(Gravity.CENTER);
            textContainer.addView(title);
        }

        if (messageText != null && !messageText.isEmpty()) {
            TextView msg = new TextView(activity);
            msg.setText(messageText);
            msg.setTextSize(14);
            msg.setTextColor(textColor);
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(0, 15, 0, 0);
            textContainer.addView(msg);
        }

        card.addView(textContainer);

        LinearLayout btnArea = new LinearLayout(activity);
        btnArea.setOrientation(LinearLayout.HORIZONTAL);
        btnArea.setLayoutParams(new LinearLayout.LayoutParams(-1, 140));

        if (leftBtnText != null && !leftBtnText.isEmpty()) {
            Button btnLeft = new Button(activity);
            btnLeft.setStateListAnimator(null);
            btnLeft.setText(leftBtnText);
            btnLeft.setTextColor(systemAccentColor);
            btnLeft.setTextSize(17);
            btnLeft.setAllCaps(false);
            btnLeft.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1f));
            
            GradientDrawable leftMask = new GradientDrawable();
            leftMask.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, cardRadius, cardRadius});
            RippleDrawable leftRipple = new RippleDrawable(ColorStateList.valueOf(rippleColor), null, leftMask);
            btnLeft.setBackground(leftRipple);
            btnLeft.setOnClickListener(v -> {
                if (leftListener != null) leftListener.onClick(v);
                dialog.dismiss();
            });
            btnArea.addView(btnLeft);
        }

        Button btnRight = new Button(activity);
        btnRight.setStateListAnimator(null);
        btnRight.setText(rightBtnText);
        btnRight.setTextColor(systemAccentColor);
        btnRight.setTextSize(17);
        btnRight.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        btnRight.setAllCaps(false);
        btnRight.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1f));

        GradientDrawable rightMask = new GradientDrawable();
        if (leftBtnText != null && !leftBtnText.isEmpty()) {
            rightMask.setCornerRadii(new float[]{0, 0, 0, 0, cardRadius, cardRadius, 0, 0});
        } else {
            rightMask.setCornerRadii(new float[]{0, 0, 0, 0, cardRadius, cardRadius, cardRadius, cardRadius});
        }
        btnRight.setBackground(new RippleDrawable(ColorStateList.valueOf(rippleColor), null, rightMask));
        btnRight.setOnClickListener(v -> {
            if (rightListener != null) rightListener.onClick(v);
            dialog.dismiss();
        });
        btnArea.addView(btnRight);

        card.addView(btnArea);
        root.addView(card);
        dialog.setContentView(root);
        dialog.setCancelable(false);

        AnimationSet iosSet = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(1.08f, 1.0f, 1.08f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        iosSet.addAnimation(scale);
        iosSet.addAnimation(alpha);
        iosSet.setDuration(120);
        iosSet.setInterpolator(new DecelerateInterpolator());
        card.startAnimation(iosSet);

        dialog.show();
    }
}