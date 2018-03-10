package Model.Entities.FamilyMember;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adima on 10/03/2018.
 */


@Entity
public class FamilyMember {
    @PrimaryKey
    @NonNull
    private String familyMemberId;

    private String albumId;
    private String email;
    private String imageUrl;
    private String serialNumber;
    public long lastUpdated;


    public FamilyMember(){

    }

    public FamilyMember(@NonNull String familyMemberId, String albumId, String email, String imageUrl, String serialNumber, long lastUpdated) {
        this.familyMemberId = familyMemberId;
        this.albumId = albumId;
        this.email = email;
        this.imageUrl = imageUrl;
        this.serialNumber = serialNumber;
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(@NonNull String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public FamilyMember (Map<String,Object> familyMemberFromFirebase){
        this.setEmail((String)familyMemberFromFirebase.get("email"));
        this.setAlbumId((String)familyMemberFromFirebase.get("albumId"));
        this.setImageUrl((String)familyMemberFromFirebase.get("imageUrl"));
        this.setLastUpdated((long)familyMemberFromFirebase.get("lastUpdated"));
        this.setFamilyMemberId((String)familyMemberFromFirebase.get("familyMemberId"));
        this.setSerialNumber((String)familyMemberFromFirebase.get("serialNumber"));


    }

    public HashMap<String,Object> toJson(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("email",this.email);
        result.put("albumId",this.albumId);
        result.put("imageUrl",this.imageUrl);
        result.put("lastUpdated",this.lastUpdated);
        result.put("familyMemberId",this.familyMemberId);
        result.put("serialNumber",this.serialNumber);

        return result;
    }

}
