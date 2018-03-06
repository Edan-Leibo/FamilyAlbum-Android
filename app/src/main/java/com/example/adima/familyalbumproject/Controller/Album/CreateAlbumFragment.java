package com.example.adima.familyalbumproject.Controller.Album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.R;

/**
 * Created by adima on 03/03/2018.
 */

public class CreateAlbumFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        String serialNumber;
        Album album = new Album();
        album.setSerialNumber();
        album.setDate();
        album.setName();
        album.setLocation();
        Model.instance().addAlbumToFirebase(album,serialNumber);
*/
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAlbumsFragment();
            }
        });

        view.findViewById(R.id.btn_create_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAlbumFragment();
            }
        });
    }
}
