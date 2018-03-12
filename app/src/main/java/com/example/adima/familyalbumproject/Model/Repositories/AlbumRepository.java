package com.example.adima.familyalbumproject.Model.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Entities.Album.AlbumFirebase;
import com.example.adima.familyalbumproject.Controller.Start.MyApplication;

import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */
/*
This class represnets the repository of an album
 */
public class AlbumRepository {


    public static final AlbumRepository instance = new AlbumRepository();

    AlbumRepository() {
    }
    MutableLiveData<List<Album>> albumsListliveData;

    /*
    Delete albums from the cache
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

    /**
     * Remove an album from local db
     * @param album
     */
    public void removeFromLocalDb(Album album){
        AlbumRepository.MyDelete delete= new AlbumRepository.MyDelete();
        delete.execute(album);

    }

    /**
     * Get all the albums
     * @param serialNumber
     * @return
     */
    public LiveData<List<Album>> getAllAlbums(final String serialNumber) {
        synchronized (this) {
                albumsListliveData = new MutableLiveData<List<Album>>();
                //1. get the last update date
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateComments", 0);
                } catch (Exception e) {

                }
                AlbumFirebase.getAllAlbumsAndObserve(serialNumber, lastUpdateDate, new AlbumFirebase.Callback<List<Album>>() {
                    @Override
                    public void onComplete(List<Album> data) {
                        updateAlbumDataInLocalStorage(data, serialNumber);
                    }
                });
        }

        return albumsListliveData;

    }

    /**
     * Update the local db with new albums
     * @param data
     * @param serialNumber
     */
    private void updateAlbumDataInLocalStorage(List<Album> data,String serialNumber) {
        Log.d("TAG", "got items from firebase: " + data.size());
        AlbumRepository.MyTask task = new AlbumRepository.MyTask();

        task.setSerialNumber(serialNumber);

        task.execute(data);
    }

    class MyTask extends AsyncTask<List<Album>,String,List<Album>> {
        private String serialNumber;

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }
        @Override
        protected List<Album> doInBackground(List<Album>[] lists) {
            Log.d("TAG","starting updateAlbumDataInLocalStorage in thread");
            if (lists.length > 0) {
                List<Album> data = lists[0];
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateComments"+serialNumber, 0);

                }catch (Exception e){

                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;
                    for (Album album : data) {
                        if (album.getAlbumId() != null) {
                            AppLocalStore.db.albumDao().insertAll(album);
                            if (album.getLastUpdated() > reacentUpdate) {
                                reacentUpdate = album.getLastUpdated();
                            }
                            Log.d("TAG", "updating: " + album.toString());
                        }
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDateComments"+serialNumber, reacentUpdate);
                    editor.commit();
                }
                //return the complete student list to the caller
                List<Album> albumsList = AppLocalStore.db.albumDao().loadAllByIds(serialNumber);
                Log.d("TAG",""+albumsList.size());
                Log.d("TAG","finish updateAlbumsDataInLocalStorage in thread");
                List<Album> albumsOldList = AppLocalStore.db.albumDao().loadAllByIds(serialNumber);
                for(Album a : albumsOldList){
                    removeFromLocalDb(a);
                }

                return albumsList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            albumsListliveData.setValue(albums);
            Log.d("TAG","update updateAlbumDataInLocalStorage in main thread");

        }
    }

}
