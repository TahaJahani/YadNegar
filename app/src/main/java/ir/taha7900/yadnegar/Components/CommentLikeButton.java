package ir.taha7900.yadnegar.Components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import ir.taha7900.yadnegar.Drawables.LikeDrawable;
import ir.taha7900.yadnegar.R;

import static ir.taha7900.yadnegar.Utils.AndroidUtilities.dp;

public class CommentLikeButton extends View {

    private boolean liked;
    private final LikeDrawable heartShape = new LikeDrawable();
    private final float height = dp(17.5f);
    private final float width = dp(14.3f);

    public CommentLikeButton(Context context, @Nullable AttributeSet attrs) {
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
        int h = resolveSize((int) height, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        heartShape.draw(canvas, (int) width, (int) height, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GestureDetector detector = new GestureDetector(CommentLikeButton.this.getContext(), new CommentLikeButton.TapListener());
        boolean result = detector.onTouchEvent(event);
        if (result) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                this.liked = !this.liked;
                heartShape.setFilled(liked);
                invalidate();
                requestLayout();
                performClick();
            }
        }
        return result;
    }

    public void toggleLike() {
        this.liked = !this.liked;
        heartShape.setFilled(liked);
        invalidate();
        requestLayout();
        performClick();
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
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
