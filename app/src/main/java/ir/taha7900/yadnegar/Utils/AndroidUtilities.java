package ir.taha7900.yadnegar.Utils;

import android.content.Context;

public class AndroidUtilities {

    private static float density;
    private static float fontScale;
    public static float dp(float px) {
        return px * density;
    }

    public static float px(float dp) {
        return dp / density;
    }

    public static void setDensityFromContext(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        fontScale = context.getResources().getConfiguration().fontScale;
    }
}
