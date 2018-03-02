package Model;


import java.util.Map;

/**
 * Created by adima on 01/03/2018.
 */

public class Album {
    public String name;
    public String albumId;
    public String date;
    public String location;
    public String serialNumber;

    public Album (String name,String date,String location,String serialNumber){
        this.name=name;
        this.date = date;
        this.location=location;
        this.serialNumber=serialNumber;
    }

    public Album (){

    }

    public Album (Map<String,Object> albumFromFirebase){
        this.albumId = (String)albumFromFirebase.get("albumId");
        this.serialNumber=(String)albumFromFirebase.get("serialNumber");
        this.date=(String)albumFromFirebase.get("date");
        this.location=(String)albumFromFirebase.get("location");
        this.name=(String)albumFromFirebase.get("name");



    }


}
