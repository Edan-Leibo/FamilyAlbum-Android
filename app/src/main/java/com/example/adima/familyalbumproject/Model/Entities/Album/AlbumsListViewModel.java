package com.example.adima.familyalbumproject.Model.Entities.Album;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.adima.familyalbumproject.Model.SQL.AlbumRepository;

import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */

public class AlbumsListViewModel extends ViewModel {

    private LiveData<List<Album>> albums;

    public void init(String serialNumber){
        albums = AlbumRepository.instance.getAllAlbums(serialNumber);
    }

    public AlbumsListViewModel(){

    }

    public LiveData<List<Album>> getAlbumList(){
        return albums;
    }



}
