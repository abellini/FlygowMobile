package br.com.flygowmobile.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import br.com.flygowmobile.enums.FontTypeEnum;

/**
 * Created by Tiago Rocha Gomes on 01/11/14.
 */
public class FontUtils {
    private Context context;

    public FontUtils(Context activity){
        this.context = activity;
    }

    public void applyFont(FontTypeEnum font, TextView... view){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), font.getPath() );
        for (TextView tv : view){
            tv.setTypeface(typeface);
        }
    }
}
