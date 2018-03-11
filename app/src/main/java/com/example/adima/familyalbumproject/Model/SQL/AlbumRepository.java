package com.example.adima.familyalbumproject.Model.SQL;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.MyApplication;

import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */

public class AlbumRepository {


    public static final AlbumRepository instance = new AlbumRepository();

    AlbumRepository() {
    }

    MutableLiveData<List<Album>> albumsListliveData;

    public LiveData<List<Album>> getAlbumsList(String serialNumber) {
        synchronized (this) {
            //if (albumsListliveData == null) {
            Log.d("TAG", "album live data is null");

            albumsListliveData = new MutableLiveData<List<Album>>();

            AlbumFirebase.getAllAlbumsAndObserve(serialNumber, new AlbumFirebase.Callback<List<Album>>() {
                @Override
                public void onComplete(List<Album> data) {

                    if (data != null) albumsListliveData.setValue(data);


                }


            });
            //}
        }
        return albumsListliveData;
    }


    public LiveData<List<Album>> getAllAlbums(final String serialNumber) {
        synchronized (this) {
            albumsListliveData = new MutableLiveData<List<Album>>();

            //1. get the last update date
            long lastUpdateDate = 0;
            try {
                lastUpdateDate = MyApplication.getMyContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateAlbums" + serialNumber, 0);
            } catch (Exception e) {
                Log.d("TAG", "exeption");

            }

            AlbumFirebase.getAllAlbumsAndObserve(serialNumber, new AlbumFirebase.Callback<List<Album>>() {
                @Override
                public void onComplete(List<Album> data) {
                    updateAlbumDataInLocalStorage(data, serialNumber);

                }
            });

        }
        return albumsListliveData;
    }

    /*
    update the cache
     */
    public void updateAlbumDataInLocalStorage(List<Album> data, String serialNumber) {
        Log.d("TAG", "got items from firebase: " + data.size());
        MyTask task = new MyTask();
        task.setSerialNumber(serialNumber);
        task.execute(data);
    }

    /*
    delete albums from cache
    */
    class MyDelete extends AsyncTask<Album,String,Boolean> {


        @Override
        protected Boolean doInBackground(Album... albums) {
            Log.d("TAG","starting delte from local storage in thread");
            if (albums!=null) {

                for (Album album : albums) {

                    AppLocalStore.db.albumDao().delete(album);

                }

            }
            return true;

        }
    }


    public void removeFromLocalDb(Album album,String serialNumber) {
        AlbumRepository.MyDelete delete= new AlbumRepository.MyDelete();
        delete.execute(album);

    }


    class MyTask extends AsyncTask<List<Album>, String, List<Album>> {
        private String serialNumber;

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        //
        @Override
        protected List<Album> doInBackground(List<Album>[] lists) {
            Log.d("TAG", "starting updateAlbumDataInLocalStorage in thread");
            if (lists.length > 0) {
                List<Album> data = lists[0];
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateAlbums" + serialNumber, 0);

                    Log.d("Tag", "got the last update date");
                } catch (Exception e) {
                    Log.d("Tag", "in the exception");


                }

                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;

                    for (Album album : data) {
                        if (album.getAlbumId() != null) {


                            AppLocalStore.db.albumDao().insertAll(album);
                            Log.d("Tag", "after insert all");


                            if (album.lastUpdated > reacentUpdate) {
                                reacentUpdate = album.lastUpdated;
                            }
                            Log.d("TAG", "updating: " + album.toString());
                        }
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDateAlbums" + serialNumber, reacentUpdate);
                    editor.commit();
                }

                //return the complete student list to the caller
                List<Album> albumsList = AppLocalStore.db.albumDao().loadAllByIds(serialNumber);
                Log.d("TAG", "finish updateEmployeeDataInLocalStorage in thread");

                return albumsList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            albumsListliveData.setValue(albums);
            Log.d("TAG", "update updateAlbumDataInLocalStorage in main thread");
            Log.d("TAG", "got items from local db: " + albums.size());

        }
    }

}
