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
 * Property objects defines the properties managed by the real estate
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

    /**
     * This method is needed to correctly parse the coordinates retrieved from a FirebaseSnapshot
     * @param coord
     * @return
     */
    public Double parseCoord(DataSnapshot coord){
        Double Dcoord;
        if(coord.getValue() instanceof Double && coord != null){
            // if it's a string the value is not correct

            Dcoord =  coord.getValue(Double.class);
        }else{
            Dcoord = 0.0;
        }
        return Dcoord;
    }

    /**
     * create a LatLng object used to generate the marker in the map
     * @param lat
     * @param lng
     */
    public void setCoords(Double lat, Double lng){
        if(lat!=null && lng!=null)
            this.coords = new LatLng(lat, lng);
    }

    /**
     * Given a Firebase snapShot return a Property object with the relative properties set
     *
     * @param snapshot
     * @param collection
     * @return
     */
    public static Property fromSnapShot(DataSnapshot snapshot, String collection){
        Property prop = snapshot.getValue(Property.class);
        prop.key = snapshot.getKey();
        if(prop.mainImageSrc!=null){
            prop.bmp = utils.loadBitmap(prop.mainImageSrc);
        }
        prop.type=collection;
        return prop;
    }

    /**
     * The following is needed to correctly convert the Property object into a hashMap that will
     * be saved into a FirebaseReference
     * @return
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        String displayFrequency = (type.equals("BUY")) ? "" :(frequency.equals("Weekly")) ? "pcw" : "pcm";
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
