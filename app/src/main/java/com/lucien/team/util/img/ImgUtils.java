package com.lucien.team.util.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.lucien.team.util.common.CommonLog;


/**
 * Created by lucien.li on 2015/6/12.
 */
public class ImgUtils {

    private static final String CLASSTAG = ImgUtils.class.getSimpleName();
    private static Bitmap imgBitmap;

    public static void setBackImage(Context context, String backImageUrl, final ImageView view) {
        // get loader
        AsyncImageLoader loader = new AsyncImageLoader(context);
        // cache image to external folder
        loader.setCacheToFile(true);
        // set external cache folder path
        loader.setCacheDir(context.getCacheDir().getAbsolutePath());

        CommonLog.i(CLASSTAG, "External path = " + context.getCacheDir().getAbsolutePath());

        // start load image
        loader.downloadImage(backImageUrl, true, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                if (bitmap != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    } else {
                        view.setBackground(new BitmapDrawable(null, bitmap));
                    }
                } else {
                    // download failed, set default image
                    CommonLog.e(CLASSTAG, "Download image failed!");
                }
            }
        });
    }

    public static Bitmap getImageBitmap(Context context, String imageUrl) {
        // get loader
        AsyncImageLoader loader = new AsyncImageLoader(context);
        // cache image to external folder
        loader.setCacheToFile(true);
        // set external cache folder path
        loader.setCacheDir(context.getCacheDir().getAbsolutePath());

        CommonLog.i(CLASSTAG, "External path = " + context.getCacheDir().getAbsolutePath());

        // start load image
        loader.downloadImage(imageUrl, true, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                if (bitmap != null) {
                    imgBitmap = bitmap;
                } else {
                    imgBitmap = null;
                    // download failed, set default image
                    CommonLog.e(CLASSTAG, "Download image failed!");
                }
            }
        });

        return imgBitmap;
    }

    public static void setImage(Context context, String imageUrl,
                                final ImageView view) {
        // get loader
        AsyncImageLoader loader = new AsyncImageLoader(context);
        // cache image to external folder
        loader.setCacheToFile(true);
        // set external cache folder path
        loader.setCacheDir(context.getCacheDir().getAbsolutePath());

        CommonLog.i(CLASSTAG, "External path = " + context.getCacheDir().getAbsolutePath());

        // start load image
        loader.downloadImage(imageUrl, true, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                if (bitmap != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        view.setImageDrawable(new BitmapDrawable(bitmap));
                    } else {
                        view.setImageDrawable(new BitmapDrawable(null, bitmap));
                    }
                } else {
                    // download failed, set default image
                    CommonLog.e(CLASSTAG, "Download image failed!");
                }
            }
        });
    }

}
