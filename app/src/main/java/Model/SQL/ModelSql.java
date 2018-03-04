package Model.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adima.familyalbumproject.Album.Model.Album;

import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */

public class ModelSql  {
    private MyOpenHelper helper;

    public ModelSql(Context context){
       // this.helper= new MyOpenHelper(context);

    }




    class MyOpenHelper extends SQLiteOpenHelper{

        public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }



        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }



    public void addAlbum(Album album){

    }

    public List<Album>  getAllAlbums(){
        return null;

    }


}

