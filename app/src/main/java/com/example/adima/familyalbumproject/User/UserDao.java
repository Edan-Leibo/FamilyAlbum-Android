package com.example.adima.familyalbumproject.User;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by adima on 06/03/2018.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE emailUser IN (:emailUser)")
    List<User> loadAllByIds(String emailUser);

    @Query("SELECT * FROM User WHERE emailUser = :emailUser")
    User findById(String emailUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Delete
    void delete(User user);



}
