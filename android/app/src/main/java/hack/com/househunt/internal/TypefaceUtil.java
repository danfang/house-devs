package hack.com.househunt.internal;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Util used in overriding the default typeface.
 */
public class TypefaceUtil {

    /**
     * Using reflection to override default typeface
     *
     * @param context                    to work with assets
     * @param defaultFontNameToOverride  for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            AssetManager a = context.getAssets();
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            System.out.println();
        }
    }
}