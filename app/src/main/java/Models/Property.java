package Models;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import Helpers.utils;

/**
 * Created by david on 10/10/2016.
 */



public class Property {

    public String type,displayAddress, summary, propertyTypeFullDescription, mainImageSrc,transactionType,propertySubType,key,frequency,displayAmount;
    public long amount;
    public ArrayList<String> images;
    public int bedrooms;
    public Bitmap bmp;

    public Property() {
        // Default constructor required for calls to DataSnapshot.getValue(Property.class)
    }

    public static Property fromSnapShot(DataSnapshot snapshot, String collection){
        Property prop = snapshot.getValue(Property.class);
        prop.key = snapshot.getKey();
        if(prop.mainImageSrc!=null){
            prop.bmp = utils.loadBitmap(prop.mainImageSrc);
        }
        prop.type=collection;
        return prop;
    }

}
