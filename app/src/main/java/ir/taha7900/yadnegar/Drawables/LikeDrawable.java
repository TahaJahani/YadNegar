package ir.taha7900.yadnegar.Drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class LikeDrawable {
    private final Paint  p  = new Paint();
    private final Paint  ps = new Paint();
    private final Path   t  = new Path();
    private final Matrix m  = new Matrix();
    private float od;
    private boolean filled = false;

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void draw(Canvas c, int w, int h, int dx, int dy){
        float ow = 683f;
        float oh = 606f;

        od = Math.min(w / ow, h / oh);

        r();
        c.save();
        c.translate((w - od * ow) / 2f + dx, (h - od * oh) / 2f + dy);

        m.reset();
        m.setScale(od, od);

        c.save();
        ps.setColor(Color.argb(0,0,0,0));
        ps.setStrokeCap(Paint.Cap.BUTT);
        ps.setStrokeJoin(Paint.Join.MITER);
        ps.setStrokeMiter(4.0f*od);
        c.scale(1.0f,1.0f);
        c.save();
        c.restore();
        r(2,1,3,0);
        c.save();
        p.setColor(Color.parseColor("#FF0000"));
        ps.setColor(Color.parseColor("#000000"));
        ps.setStrokeWidth(25.0f*od);
        ps.setStrokeCap(Paint.Cap.ROUND);
        ps.setStrokeJoin(Paint.Join.ROUND);
        ps.setStrokeMiter(10.0f*od);
        t.reset();
        t.moveTo(654.0f,166.6f);
        t.cubicTo(649.3f,140.4f,639.2f,116.3f,623.4f,94.8f);
        t.cubicTo(608.5f,74.5f,590.0f,58.3f,567.5f,46.7f);
        t.cubicTo(549.3f,37.3f,530.0f,31.7f,509.6f,29.4f);
        t.cubicTo(483.3f,26.4f,457.8f,29.5f,433.3f,39.6f);
        t.cubicTo(400.6f,53.0f,375.7f,76.1f,355.9f,105.0f);
        t.cubicTo(351.0f,112.1f,346.5f,119.5f,341.9f,126.8f);
        t.cubicTo(340.3f,125.7f,339.6f,124.0f,338.8f,122.4f);
        t.cubicTo(332.9f,111.8f,326.0f,101.8f,318.4f,92.2f);
        t.cubicTo(303.0f,72.9f,285.0f,56.9f,263.0f,45.5f);
        t.cubicTo(237.0f,32.0f,209.2f,26.8f,179.9f,29.1f);
        t.cubicTo(143.5f,32.0f,111.2f,44.7f,84.1f,69.3f);
        t.cubicTo(44.7f,105.0f,27.1f,150.4f,27.2f,202.9f);
        t.cubicTo(27.3f,247.3f,44.9f,285.3f,71.9f,319.5f);
        t.cubicTo(95.4f,349.3f,123.3f,374.9f,151.4f,400.2f);
        t.cubicTo(184.5f,429.9f,217.9f,459.2f,250.0f,490.0f);
        t.cubicTo(276.9f,515.7f,302.1f,542.9f,326.2f,571.2f);
        t.cubicTo(330.4f,576.1f,335.1f,579.7f,341.9f,579.3f);
        t.cubicTo(348.8f,580.1f,353.6f,576.3f,357.6f,571.5f);
        t.cubicTo(398.9f,522.2f,445.5f,478.2f,493.3f,435.4f);
        t.cubicTo(525.3f,406.8f,558.0f,378.8f,587.7f,347.7f);
        t.cubicTo(607.1f,327.4f,624.5f,305.6f,637.4f,280.5f);
        t.cubicTo(656.2f,244.4f,661.1f,206.3f,654.0f,166.6f);

        t.transform(m);
        if (filled)
            c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(2,1,3,0);
        p.setColor(Color.parseColor("#FF0000"));
        ps.setColor(Color.parseColor("#000000"));
        ps.setStrokeWidth(50.0f*od);
        ps.setStrokeCap(Paint.Cap.ROUND);
        ps.setStrokeJoin(Paint.Join.ROUND);
        ps.setStrokeMiter(10.0f*od);
        c.restore();
        r();

        c.restore();
    }

    private void r(Integer... o){
        p.reset();
        ps.reset();
        p.setAntiAlias(true);
        ps.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        ps.setStyle(Paint.Style.STROKE);
        for(Integer i : o){
            switch (i){
                case 0: ps.setStrokeMiter(4.0f*od); break;
                case 1: ps.setStrokeCap(Paint.Cap.BUTT); break;
                case 2: ps.setColor(Color.argb(0,0,0,0)); break;
                case 3: ps.setStrokeJoin(Paint.Join.MITER); break;
            }
        }
    }
}