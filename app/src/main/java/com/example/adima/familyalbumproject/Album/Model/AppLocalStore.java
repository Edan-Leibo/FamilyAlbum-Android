package com.example.adima.familyalbumproject.Album.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.adima.familyalbumproject.Entities.Album;
import com.example.adima.familyalbumproject.MyApplication;

/**
 * Created by adima on 02/03/2018.
 */

@Database(entities = {Album.class}, version = 2)
abstract class AppLocalStoreDb extends RoomDatabase {
    public abstract AlbumDao albumDao();
}

public class AppLocalStore{
    static public AppLocalStoreDb db = Room.databaseBuilder(MyApplication.getMyContext(),
            AppLocalStoreDb.class,
            "database-name").fallbackToDestructiveMigration().build();
}