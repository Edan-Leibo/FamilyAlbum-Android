package com.example.adima.familyalbumproject.Album.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */
@Dao
public interface AlbumDao {
    @Query("SELECT * FROM Album")
    List<Album> getAll();

    @Query("SELECT * FROM Album WHERE albumId IN (:albumId)")
    List<Album> loadAllByIds(int[] albumId);



    @Query("SELECT * FROM Album WHERE serialNumber IN (:serialNumber)")
    List<Album> loadAllByIds(String serialNumber);

    @Query("SELECT * FROM Album WHERE albumId = :id")
    Album findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Album... albums);

    @Delete
    void delete(Album album);
}
