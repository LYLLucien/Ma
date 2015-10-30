package com.lucien.team.util.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;


import com.lucien.team.util.common.CommonLog;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncImageLoader {

    private static final String CLASSTAG = AsyncImageLoader.class.getSimpleName();

    // To avoid download delicately, save urls of downloading image
    private static HashSet<String> mDownloadingSet = new HashSet<>();
    // image manager, images could be got by three ways, which are from web url, memory cache or external file cache
    private static LoaderImpl impl = new LoaderImpl();
    // thread pool related
    private static ExecutorService mExecutorService;
    // handler, using after ui thread gets image finish
    private Handler handler;

    /**
     * async load image done callback interface, which is used for notify activity
     */
    public interface ImageCallback {
        void onImageLoaded(Bitmap bitmap, String imageUrl);
    }

    public AsyncImageLoader(Context context) {
        handler = new Handler();
        startThreadPoolIfNecessary();
        String defaultDir = context.getCacheDir().getAbsolutePath();
        setCacheDir(defaultDir);
    }

    /**
     * is cache image to /data/data/packageName/cache/path
     * default not cache
     *
     * @param flag
     */
    public void setCacheToFile(boolean flag) {
        impl.setCacheToFile(flag);
    }

    /**
     * set cache path, valid when setCacheToFile(true)
     *
     * @param dir
     */
    public void setCacheDir(String dir) {
        impl.setCacheDir(dir);
    }

    /**
     * start thread pool
     */
    public static void startThreadPoolIfNecessary() {
        if (mExecutorService == null || mExecutorService.isShutdown() || mExecutorService.isTerminated()) {
            mExecutorService = Executors.newFixedThreadPool(3);
        }
    }

    /**
     * async download image, and cache to memory
     *
     * @param url
     * @param callback
     */
    public void downloadImage(final String url, final ImageCallback callback) {
        downloadImage(url, true, callback);
    }

    /**
     * @param url
     * @param cacheToMemory is cache to memory
     * @param callback
     */
    public void downloadImage(final String url, final boolean cacheToMemory, final ImageCallback callback) {
        if (mDownloadingSet.contains(url)) {
            CommonLog.i(CLASSTAG, "image is downloading!");
            return;
        }

        // if image could be gotten
        Bitmap bitmap = impl.getBitmapFromMemory(url);
        CommonLog.i(CLASSTAG, "getBitmapFromMemory = " + bitmap);

        // if not, download from internet
        if (bitmap != null) {
            if (callback != null) {
                callback.onImageLoaded(bitmap, url);
            }
        } else {
            // download from internet
            mDownloadingSet.add(url);
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = impl.getBitmapFromUrl(url, cacheToMemory);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onImageLoaded(bitmap, url);
                            }
                            mDownloadingSet.remove(url);
                        }
                    });
                }
            });
        }
    }

    /**
     * preload next image, cache to memory
     *
     * @param url
     */
    public void preLoadNextImage(final String url) {
        downloadImage(url, null);
    }

}
