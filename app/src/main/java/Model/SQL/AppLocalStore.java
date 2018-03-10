package Model.SQL;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.adima.familyalbumproject.MyApplication;

import Model.Entities.Album.Album;
import Model.Entities.Album.AlbumDao;
import Model.Entities.Comment.Comment;
import Model.Entities.Comment.CommentDao;
import Model.Entities.FamilyMember.FamilyMember;
import Model.Entities.FamilyMember.FamilyMemberDao;
import Model.Entities.Image.Image;
import Model.Entities.Image.ImageDao;

/**
 * Created by adima on 04/03/2018.
 */

@Database(entities = {Album.class,Comment.class,Image.class, FamilyMember.class},version = 5)
abstract class AppLocalStoreDb extends RoomDatabase {
    public abstract AlbumDao albumDao();
    public abstract CommentDao commentDao();
    public abstract ImageDao imageDao();
    public abstract FamilyMemberDao familyMemberDao();
}

public class AppLocalStore {

    static public AppLocalStoreDb db = Room.databaseBuilder(MyApplication.getMyContext(),AppLocalStoreDb.class,"database-name").fallbackToDestructiveMigration().build();

}