package com.makasart.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Maxim on 28.10.2016.
 */

@SuppressWarnings("deprecation")
public class PictureUtils {
    public static BitmapDrawable getScaledDrawable(Activity a, String path) {
        Display display = a.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcHeight > destHeight) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(a.getResources(), bitmap);
    }


    public static void cleanImageView(ImageView imageView){
        if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
            return;
        }
        BitmapDrawable bD = (BitmapDrawable)imageView.getDrawable();
        bD.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
