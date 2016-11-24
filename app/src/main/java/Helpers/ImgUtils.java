package Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.IOException;
import java.net.URL;

import db.FirebaseH;

/**
 * Utils class is used to download image file from the web
 * and cache them for future use
 */

public class ImgUtils {

    /**
     * memoryCache is a singleton object, it handles the cache of downloaded images
     *
     */

    private static class memoryCache{
        private LruCache<String, Bitmap> mMemoryCache;


        private static memoryCache ourInstance = new memoryCache();
        public static memoryCache getInstance() {
            return ourInstance;
        }

        private memoryCache(){
            // Get the max memoty from the system
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;

            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            // Add the image to the cache if it's not present
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        // Retrieve the image from the cache
        public Bitmap getBitmapFromMemCache(String key) {
            return mMemoryCache.get(key);
        }
    }

    public static Bitmap loadBitmap(String path) {
        memoryCache mem = memoryCache.getInstance();
        // Try if the image is in the cache, if so return the cached image
        Bitmap b = mem.getBitmapFromMemCache(path);
        if(b!=null) return b;
        try{
            // create a bitmap serving a stream of data to the BitmapFactory
            URL imgurl = new URL(path);
            b = BitmapFactory.decodeStream(imgurl.openConnection().getInputStream());
            // add the image to the cache and return
            mem.addBitmapToMemoryCache(path, b);
            return b;
        }catch (IOException e) {
            return  null;
        }
    }

}
