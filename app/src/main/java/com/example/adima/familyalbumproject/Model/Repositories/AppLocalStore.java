package com.example.adima.familyalbumproject.Model.Repositories;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Entities.Album.AlbumDao;
import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Entities.Comment.CommentDao;
import com.example.adima.familyalbumproject.Model.Entities.Image.Image;
import com.example.adima.familyalbumproject.Model.Entities.Image.ImageDao;

/**
 * Created by adima on 04/03/2018.
 */

@Database(entities = {Album.class,Comment.class,Image.class},version = 5)
abstract class AppLocalStoreDb extends RoomDatabase {
    public abstract AlbumDao albumDao();
    public abstract CommentDao commentDao();
    public abstract ImageDao imageDao();

    
}

public class AppLocalStore {

    static public AppLocalStoreDb db = Room.databaseBuilder(MyApplication.getMyContext(),AppLocalStoreDb.class,"database-name").fallbackToDestructiveMigration().build();

}