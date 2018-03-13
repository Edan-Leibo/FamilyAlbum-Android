package com.example.adima.familyalbumproject.Model.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.Model.Entities.Image.Image;
import com.example.adima.familyalbumproject.Model.Entities.Image.ImageFirebase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 05/03/2018.
 */
/*
This class represnts the image repository
 */

public class ImageRepository {

    public static final ImageRepository instance = new ImageRepository();

    ImageRepository() {
    }
    MutableLiveData<List<Image>> imagesListliveData;

    public LiveData<List<Image>> getAllImages(final String albumId) {
        synchronized (this) {

            imagesListliveData = new MutableLiveData<List<Image>>();

            //1. get the last update date
            long lastUpdateDate = 0;
            try {
                lastUpdateDate = MyApplication.getMyContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateImages"+albumId, 0);
            }catch (Exception e){
            }

            ImageFirebase.observeAllImages(albumId, lastUpdateDate, new ImageFirebase.CallbackOnImageUpdate<Image>() {

                @Override
                public void onDeleted(Image data) {
                    List<Image> list = new LinkedList<>();
                    list.add(data);
                    deleteImageDataInLocalStorage(list,albumId);
                }

                @Override
                public void dataChanged(List<Image> list) {
                    addImageDataInLocalStorage(list,albumId);
                }
            });
        }
        return imagesListliveData;
    }


    private void addImageDataInLocalStorage(List<Image> data, String albumId) {
        AddingTask task = new AddingTask();
        task.setAlbumId(albumId);
        task.execute(data);
    }

    private void deleteImageDataInLocalStorage(List<Image> data,String albumId) {
        DeletionTask task = new DeletionTask();
        task.setAlbumId(albumId);
        task.execute(data);
    }

    public void removeFromLocalDb(Image image) {
        List<Image> list = new LinkedList<>();
        list.add(image);
        deleteImageDataInLocalStorage(list,image.getAlbumId());
    }

    ///
    class GetAllTask extends AsyncTask<List<Image>,String,List<Image>> {

        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        @Override
        protected List<Image> doInBackground(List<Image>[] lists) {

            List<Image> imagesList = AppLocalStore.db.imageDao().loadAllByIds(albumId);
            Log.d("TAG","finish updateEmployeeDataInLocalStorage in thread");

            return imagesList;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            super.onPostExecute(images);
            imagesListliveData.setValue(images);
        }
    }

    ////

    class DeletionTask extends AsyncTask<List<Image>,String,List<Image>> {

        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        @Override
        protected List<Image> doInBackground(List<Image>[] lists) {
            List<Image> data = lists[0];

            for (Image image : data) {
                AppLocalStore.db.imageDao().delete(image);
            }

            List<Image> imagesList = AppLocalStore.db.imageDao().loadAllByIds(albumId);
            return imagesList;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            super.onPostExecute(images);
            imagesListliveData.setValue(images);
        }
    }



    /////
    class AddingTask extends AsyncTask<List<Image>,String,List<Image>> {
        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }
        @Override
        protected List<Image> doInBackground(List<Image>[] lists) {
            Log.d("TAG","starting updateImageDataInLocalStorage in thread");
            if (lists.length > 0) {
                List<Image> data = lists[0];
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateImages"+albumId, 0);

                    Log.d("Tag","got the last update date");
                }catch (Exception e){
                    Log.d("Tag","in the exception");


                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;

                    for (Image image : data) {
                        if (image.getImageId() != null) {

                            AppLocalStore.db.imageDao().insertAll(image);
                            Log.d("Tag", "after insert all");

                            if (image.getLastUpdated() > reacentUpdate) {
                                reacentUpdate = image.getLastUpdated();
                            }
                            Log.d("TAG", "updating: " + image.toString());
                        }
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDateImages"+albumId, reacentUpdate);
                    editor.commit();
                }
                //return the complete student list to the caller
                List<Image> imagesList = AppLocalStore.db.imageDao().loadAllByIds(albumId);
                Log.d("TAG","finish updateEmployeeDataInLocalStorage in thread");

                return imagesList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            super.onPostExecute(images);
            imagesListliveData.setValue(images);
            Log.d("TAG","update updateAlbumDataInLocalStorage in main thread");
            Log.d("TAG", "got items from local db: " + images.size());

        }
    }

}
