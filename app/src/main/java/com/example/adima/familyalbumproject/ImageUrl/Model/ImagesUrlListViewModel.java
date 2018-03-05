package com.example.adima.familyalbumproject.ImageUrl.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.adima.familyalbumproject.Album.Model.ImageRepository;
import com.example.adima.familyalbumproject.Entities.Image;

import java.util.List;

/**
 * Created by adima on 05/03/2018.
 */

public class ImagesUrlListViewModel extends ViewModel {

    private LiveData<List<Image>> images;

    public void init(String albumID){
        images = ImageRepository.instance.getAllImages(albumID);
    }

    public ImagesUrlListViewModel() {}

    public LiveData<List<Image>> getImagesList() {
        return images;
    }
}
