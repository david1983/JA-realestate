package Models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public LatLng coords;

    public Property() {
        // Default constructor required for calls to DataSnapshot.getValue(Property.class)
    }

    public Double parseCoord(DataSnapshot coord){
        Double Dcoord;
        if(coord.getValue() instanceof String){
            Dcoord = 0.0;
        }else{
            Dcoord =  coord.getValue(Double.class);
        }
        return Dcoord;
    }

    public void setCoords(Double lat, Double lng){
        this.coords = new LatLng(lat, lng);
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        String displayFrequency = (type=="BUY") ? "" :(frequency=="Weekly") ? "pcw" : "pcm";
        result.put("propertyTypeFullDescription", propertyTypeFullDescription);
        result.put("summary", summary);
        result.put("displayAddress", displayAddress);
        result.put("amount", amount);
        result.put("frequency", frequency);
        result.put("mainImageSrc", mainImageSrc);
        result.put("type", type);
        result.put("bedrooms", bedrooms);
        result.put("displayAmount", "£" + amount + " " + displayFrequency);

        return result;
    }


}
