package com.example.adima.familyalbumproject.ImageUrl.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.adima.familyalbumproject.Entities.Image;

import java.util.List;

/**
 * Created by adima on 05/03/2018.
 */

@Dao
public interface ImageDao {
    @Query("SELECT * FROM Image")
    List<Image> getAll();

    @Query("SELECT * FROM Image WHERE albumId IN (:albumId)")
    List<Image> loadAllByIds(String albumId);

    @Query("SELECT * FROM Image WHERE albumId = :id")
    Image findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Image... images);

    @Delete
    void delete(Image image);

}
