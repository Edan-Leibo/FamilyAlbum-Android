package com.example.adima.familyalbumproject.Model.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Entities.Album.AlbumFirebase;

import java.util.LinkedList;
import java.util.List;


/*
This class represents the repository of an album
 */
public class AlbumRepository {


    public static final AlbumRepository instance = new AlbumRepository();

    AlbumRepository() {
    }
    MutableLiveData<List<Album>> albumsListliveData;


    /**
     * Get all albums
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
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateAlbums"+serialNumber, 0);
            }catch (Exception e){
            }

            AlbumFirebase.observeAllAlbums(serialNumber, lastUpdateDate, new AlbumFirebase.CallbackOnAlbumUpdate<Album>() {

                @Override
                public void onDeleted(Album data) {
                    List<Album> list = new LinkedList<>();
                    list.add(data);
                    deleteAlbumDataInLocalStorage(list,serialNumber);
                }

                @Override
                public void dataChanged(List<Album> list) {
                    addAlbumDataInLocalStorage(list,serialNumber);
                }
            });
        }
        return albumsListliveData;
    }

    /**
     * Add an album
     * @param data
     * @param serialNumber
     */
    private void addAlbumDataInLocalStorage(List<Album> data, String serialNumber) {
        AddingTask task = new AddingTask();
        task.setSerialNumber(serialNumber);
        task.execute(data);
    }

    /**
     * Delete an album
     * @param data
     * @param serialNumber
     */
    private void deleteAlbumDataInLocalStorage(List<Album> data,String serialNumber) {
        DeletionTask task = new DeletionTask();
        task.setSerialNumber(serialNumber);
        task.execute(data);
    }



    class DeletionTask extends AsyncTask<List<Album>,String,List<Album>> {

        private String serialNumber;

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        @Override
        protected List<Album> doInBackground(List<Album>[] lists) {
            List<Album> data = lists[0];

            for (Album album : data) {
                AppLocalStore.db.albumDao().delete(album);
            }

            List<Album> albumsList = AppLocalStore.db.albumDao().loadAllByIds(serialNumber);
            return albumsList;
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            albumsListliveData.setValue(albums);
        }
    }



    /////
    class AddingTask extends AsyncTask<List<Album>,String,List<Album>> {
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
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateAlbums"+serialNumber, 0);

                    Log.d("Tag","got the last update date");
                }catch (Exception e){
                    Log.d("Tag","in the exception");


                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;

                    for (Album album : data) {
                        if (album.getAlbumId() != null) {

                            AppLocalStore.db.albumDao().insertAll(album);
                            Log.d("Tag", "after insert all");

                            if (album.getLastUpdated() > reacentUpdate) {
                                reacentUpdate = album.getLastUpdated();
                            }
                            Log.d("TAG", "updating: " + album.toString());
                        }
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDateAlbums"+serialNumber, reacentUpdate);
                    editor.commit();
                }
                //return the complete student list to the caller
                List<Album> albumsList = AppLocalStore.db.albumDao().loadAllByIds(serialNumber);
                Log.d("TAG","finish updateEmployeeDataInLocalStorage in thread");

                return albumsList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            albumsListliveData.setValue(albums);
            Log.d("TAG","update updateAlbumDataInLocalStorage in main thread");
            Log.d("TAG", "got items from local db: " + albums.size());

        }
    }


}
