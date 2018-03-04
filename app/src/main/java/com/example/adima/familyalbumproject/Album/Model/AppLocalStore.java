package com.example.adima.familyalbumproject.Album.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.adima.familyalbumproject.Comment.Model.Comment;
import com.example.adima.familyalbumproject.Comment.Model.CommentDao;
import com.example.adima.familyalbumproject.MyApplication;

/**
 * Created by adima on 04/03/2018.
 */

@Database(entities = {Album.class,Comment.class},version = 3)
abstract class AppLocalStoreDb extends RoomDatabase {
    public abstract AlbumDao albumDao();
    public abstract CommentDao commentDao();
}

public class AppLocalStore {

    static public AppLocalStoreDb db = Room.databaseBuilder(MyApplication.getMyContext(),AppLocalStoreDb.class,"database-name").fallbackToDestructiveMigration().build();

}