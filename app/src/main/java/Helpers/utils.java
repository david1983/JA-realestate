package Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ic3 on 06/11/16.
 */

public class utils {

    public static Bitmap loadBitmap(String path) {
        try{
            URL newurl = new URL(path);
            return BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        }catch (IOException e) {
            return  null;
        }
    }
}
