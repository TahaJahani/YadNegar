package ir.taha7900.yadnegar.Components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import ir.taha7900.yadnegar.Drawables.LikeDrawable;

import androidx.annotation.Nullable;
import ir.taha7900.yadnegar.R;

import static ir.taha7900.yadnegar.Utils.AndroidUtilities.dp;


public class LikeButton extends View {

    private boolean liked;
    private final LikeDrawable heartShape = new LikeDrawable();
    private final float height = dp(35);
    private final float width = dp(28.6f);
    private final float jump = dp(14.3f);

    public LikeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LikeButton, 0, 0);
        try {
            this.liked = array.getBoolean(R.styleable.LikeButton_liked, false);
        } finally {
            array.recycle();
        }
    }

    public boolean isLiked() {
        return liked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = resolveSize((int) width, widthMeasureSpec);
        int h = resolveSize((int) (height + jump), heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        heartShape.setFilled(liked);
        heartShape.draw(canvas, (int) width, (int) height, 0, (int)jump);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GestureDetector detector = new GestureDetector(LikeButton.this.getContext(), new TapListener());
        boolean result = detector.onTouchEvent(event);
        if (result) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                this.liked = !this.liked;
                invalidate();
                requestLayout();
                performClick();
                if (liked)
                    clickAnimation();
            }
        }
        return result;
    }

    public void like() {
        if (!liked) {
            this.liked = !this.liked;
            heartShape.setFilled(liked);
            invalidate();
            requestLayout();
            performClick();
            clickAnimation();
        }
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
        invalidate();
        requestLayout();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void clickAnimation() {
        ObjectAnimator upAnimator = ObjectAnimator.ofFloat(this, "translationY", -jump);
        upAnimator.setInterpolator(new DecelerateInterpolator());
        upAnimator.setDuration(250);
        ObjectAnimator downAnimator = ObjectAnimator.ofFloat(this, "translationY", 0);
        downAnimator.setDuration(250);
        TimeInterpolator interpolator = new BounceInterpolator();
        downAnimator.setInterpolator(interpolator);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(upAnimator, downAnimator);
        animatorSet.start();
    }


    static class TapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }
}