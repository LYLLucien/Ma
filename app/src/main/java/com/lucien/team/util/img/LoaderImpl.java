package com.lucien.team.util.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;


import com.lucien.team.util.common.CommonLog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class LoaderImpl {

    private static final String CLASSTAG = LoaderImpl.class.getSimpleName();
    private static final String MD5 = "MD5";
    private static final String UTF8 = "UTF-8";

    // is cache image to local disk
    private boolean diskCache = true;
    // cache path, the default path is /data/data/package/cache/path
    private String cacheDir;

    // define second level cache, container, soft reference
    private static ConcurrentHashMap<String, SoftReference<Bitmap>> currentHashmap = new ConcurrentHashMap<>();
    // LruCache use hard reference put image to LinkedHashMap
    final static int memClass = (int) Runtime.getRuntime().maxMemory();

    private static LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>(memClass / 5) {
        protected int sizeOf(String key, Bitmap value) {
            if (value != null) {
                // calculate the cache bit number of bitmap
                return value.getRowBytes() * value.getHeight();
            }
            return 0;
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if (oldValue != null) {
                // if hard reference container is full, the latest not used image will transfer to
                // soft reference cache by LRU algorithm
                currentHashmap.put(key, new SoftReference<>(oldValue));
                CommonLog.i(CLASSTAG, "Image has been cached to second level reference!");
            }
        }
    };

    /**
     * is cache image to external file
     *
     * @param diskCache
     */
    public void setCacheToFile(boolean diskCache) {
        this.diskCache = diskCache;
    }

    /**
     * set external file path of cache image
     *
     * @param cacheDir
     */
    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public Bitmap getBitmapFromUrl(String urlStr, boolean cacheToMemory) {
        Bitmap bitmap;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            CommonLog.i(CLASSTAG, "Download from: " + urlStr);

            if (cacheToMemory) {
                // 1.cache bitmap to memory soft reference
                imageCache.put(urlStr, bitmap);
                if (diskCache) {
                    CommonLog.i(CLASSTAG, "Cache to local: " + urlStr);
                    // 2.cache bitmap to /data/data/packageName/cache/folder
                    String fileName = getMD5Str(urlStr);
                    String filePath = this.cacheDir + "/" + fileName;
                    FileOutputStream fos = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
            }
            is.close();
            conn.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get bitmap from memory cache
     *
     * @param urlStr
     * @return bitmap or null.
     */
    public Bitmap getBitmapFromMemory(String urlStr) {
        Bitmap bitmap;
        synchronized (imageCache) {
            bitmap = imageCache.get(urlStr);
            if (bitmap != null) {
                imageCache.remove(urlStr);
                // according to LRU, which is Least Recently Used memory algorithm, put it on top
                imageCache.put(urlStr, bitmap);
                CommonLog.i(CLASSTAG, "Image has been found in cache: " + urlStr);
                return bitmap;
            }
        }

        // search in second level cache
        SoftReference<Bitmap> soft = currentHashmap.get(urlStr);
        if (soft != null) {
            // find image in soft reference
            Bitmap softBitmap = soft.get();
            if (softBitmap != null) {
                CommonLog.i(CLASSTAG, "Image has been found in second level cache: " + urlStr);
                return softBitmap;
            }
        }

        // search in external folder
        if (diskCache) {
            bitmap = getBitmapFromFile(urlStr);
            if (bitmap != null) {
                imageCache.put(urlStr, bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap getBitmapFromFile(String urlStr) {
        Bitmap bitmap;
        String fileName = getMD5Str(urlStr);
        if (fileName == null) {
            return null;
        }
        String filePath = cacheDir + "/" + fileName;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * MD5 encryption
     *
     * @param str
     * @return
     */
    private static String getMD5Str(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.reset();
            messageDigest.update(str.getBytes(UTF8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * MD5 encryption private static String getMD5Str(Object...objects){ StringBuilder
     * stringBuilder=new StringBuilder(); for (Object object : objects) {
     * stringBuilder.append(object.toString()); } return
     * getMD5Str(stringBuilder.toString()); }
     */
}
