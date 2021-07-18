package ir.taha7900.yadnegar.Drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class CommentDrawable {
    private static final Paint  p  = new Paint();
    private static final Paint  ps = new Paint();
    private static final Path   t  = new Path();
    private static final Matrix m  = new Matrix();
    private static float od;
    protected static ColorFilter cf = null;

    /**
     *  IMPORTANT: Due to the static usage of this class this
     *  method sets the tint color statically. So it is highly
     *  recommended to call the clearColorTint method when you
     *  have finished drawing.
     *
     *  Sets the color to use when drawing the SVG. This replaces
     *  all parts of the drawable which are not completely
     *  transparent with this color.
     */
    public static void setColorTint(int color){
        cf = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void clearColorTint(int color){
        cf = null;
    }

    public static void draw(Canvas c, int w, int h){
        draw(c, w, h, 0, 0);
    }

    public static void draw(Canvas c, int w, int h, int dx, int dy){

        float ow = 188f;
        float oh = 188f;
//        float ow = 230f;
//        float oh = 230f;

        od = Math.min(w / ow, h / oh);

        r();
        c.save();
        c.translate((w - od * ow) / 2f + dx, (h - od * oh) / 2f + dy);

        m.reset();
        m.setScale(od, od);

        c.save();
        ps.setStrokeCap(Paint.Cap.BUTT);
        ps.setStrokeJoin(Paint.Join.MITER);
        ps.setStrokeMiter(4.0f*od);
        ps.setStrokeWidth(6.5f*od);
        ps.setStyle(Paint.Style.STROKE);
        c.scale(1.0f,1.0f);
        c.save();
        t.reset();

        t.moveTo(93.6f,6.8f);
        t.cubicTo(45.4f,6.8f,6.2f,46.0f,6.2f,94.2f);
        t.cubicTo(6.2f,142.4f,45.4f,181.6f,93.6f,181.6f);
        t.cubicTo(109.5f,181.6f,125.2f,177.20001f,138.7f,169.0f);
        t.cubicTo(138.8f,168.9f,138.9f,168.9f,139.0f,168.8f);
        t.cubicTo(140.5f,167.5f,142.4f,166.8f,144.3f,166.8f);
        t.cubicTo(145.0f,166.8f,145.7f,166.90001f,146.40001f,167.1f);
        t.lineTo(174.3f,174.8f);
        t.cubicTo(174.5f,174.8f,174.7f,174.90001f,174.8f,174.90001f);
        t.cubicTo(175.3f,174.90001f,175.7f,174.70001f,176.1f,174.3f);
        t.cubicTo(176.6f,173.8f,176.8f,173.2f,176.70001f,172.5f);
        t.lineTo(169.50002f,141.5f);
        t.cubicTo(169.00002f,139.3f,169.40001f,137.0f,170.70001f,135.2f);
        t.cubicTo(170.70001f,135.09999f,170.80002f,135.09999f,170.80002f,135.0f);
        t.cubicTo(177.40002f,122.4f,180.90002f,108.4f,180.90002f,94.1f);
        t.cubicTo(181.0f,46.0f,141.8f,6.8f,93.6f,6.8f);
        t.close();

        t.transform(m);
        c.drawPath(t, ps);
        c.restore();
        r(1,2,3,0);
        c.restore();
        r();

        c.restore();
    }

    private static void r(Integer... o){
        p.reset();
        ps.reset();
        if(cf != null){
            p.setColorFilter(cf);
            ps.setColorFilter(cf);
        }
        p.setAntiAlias(true);
        ps.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        ps.setStyle(Paint.Style.STROKE);
        for(Integer i : o){
            switch (i){
                case 0: ps.setStrokeMiter(4.0f*od); break;
                case 1: ps.setColor(Color.argb(0,0,0,0)); break;
                case 2: ps.setStrokeCap(Paint.Cap.BUTT); break;
                case 3: ps.setStrokeJoin(Paint.Join.MITER); break;
            }
        }
    }
}