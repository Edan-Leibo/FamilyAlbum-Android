package com.example.adima.familyalbumproject.Model.Entities.Comment;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> getAll();

    @Query("SELECT * FROM Comment WHERE albumId IN (:albumId)")
    List<Comment> loadAllByIds(String albumId);

    @Query("SELECT * FROM Comment WHERE albumId = :id")
    Comment findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment... comments);

    @Delete
    void delete(Comment comment);


}
