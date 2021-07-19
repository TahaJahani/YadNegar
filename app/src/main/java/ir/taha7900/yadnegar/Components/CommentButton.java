package ir.taha7900.yadnegar.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import ir.taha7900.yadnegar.Drawables.CommentDrawable;

import static ir.taha7900.yadnegar.Utils.AndroidUtilities.dp;

public class CommentButton extends View {

    private final float height = dp(35);
    private final float width = dp(28.6f);

    public CommentButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = resolveSize((int) width, widthMeasureSpec);
        int h = resolveSize((int) height, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CommentDrawable.draw(canvas, (int)width, (int)height);
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
    }
}