package Model.Entities.Album;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adima on 01/03/2018.
 */

@Entity
public class Album {

    @PrimaryKey
    @NonNull
    public String albumId;

    public String name;
    public String date;
    public String location;
    public String serialNumber;
    public long lastUpdated;
    public Album (){

    }


    public Album (String name,String date,String location,String serialNumber){
        this.name=name;
        this.date = date;
        this.location=location;
        this.serialNumber=serialNumber;
    }

    public Album(@NonNull String albumId, String name, String date, String location, String serialNumber, long lastUpdated) {
        this.albumId = albumId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.serialNumber = serialNumber;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public Album (Map<String,Object> albumFromFirebase){
        this.albumId = (String)albumFromFirebase.get("albumId");
        this.serialNumber=(String)albumFromFirebase.get("serialNumber");
        this.date=(String)albumFromFirebase.get("date");
        this.location=(String)albumFromFirebase.get("location");
        this.name=(String)albumFromFirebase.get("name");
        this.lastUpdated=(long)albumFromFirebase.get("lastUpdated");
    }


    public HashMap<String,Object> toJson(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("albumId",albumId);
        result.put("name",name);
        result.put("date",date);
        result.put("location",location);
        result.put("serialNumber",serialNumber);
        result.put("lastUpdated",lastUpdated);

        return result;
    }


}
