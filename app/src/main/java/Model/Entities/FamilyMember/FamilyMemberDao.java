package Model.Entities.FamilyMember;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by adima on 10/03/2018.
 */

@Dao
public interface FamilyMemberDao {

    @Query("SELECT * FROM FamilyMember")
    List<FamilyMember> getAll();

    @Query("SELECT * FROM FAMILYMEMBER WHERE serialNumber IN (:serialNumber)")
    List<FamilyMember> loadAllByIds(String serialNumber);

    @Query("SELECT * FROM FamilyMember WHERE familyMemberId = :familyMemberId")
    FamilyMember findById(String familyMemberId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FamilyMember... comments);

    @Delete
    void delete(FamilyMember familyMember);
}
