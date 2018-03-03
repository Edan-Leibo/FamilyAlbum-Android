package com.example.adima.familyalbumproject.Controller.Album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.Entities.Album;
import com.example.adima.familyalbumproject.R;

import static android.view.View.GONE;

/**
 * Created by adima on 03/03/2018.
 */

public class AlbumFragment extends Fragment {

    ImageView imageView;
    Bitmap imageBitmap;
    private Album album;

    public static AlbumFragment newInstance(Album album) {
        AlbumFragment albumFragment = new AlbumFragment();
        albumFragment.album = album;
        return albumFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAlbumsFragment();
            }
        });
        view.findViewById(R.id.btn_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showCommentsFragment(album);
            }
        });

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new AlbumGridAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


        //album.getImages()

        ImageView addPhotoBtn = (ImageView) view.findViewById(R.id.fragment_album_btn_add);
        //Button cancelBtn = (Button) findViewById(R.id.mainCancelBtn);

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);

                Log.d("TAG","Btn add click");
                dispatchTakePictureIntent();
            }
        });


        ///
    }




    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESULT_SUCCESS = 0;
    //final static int RESULT_FAIL = 1;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_SUCCESS) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //imageView.setImageBitmap(imageBitmap);
            Model.instace.saveImage(imageBitmap, st.id + ".jpeg", new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    //st.imageUrl = url;
                    //Model.instace.addStudent(st);
                    //setResult(RESULT_SUCCESS);
                    //progressBar.setVisibility(GONE);
                    finish();
                }

                @Override
                public void fail() {
                    //notify operation fail,...
                    //setResult(RESULT_SUCCESS);
                    //progressBar.setVisibility(GONE);
                    finish();
                }
        }
    }
}
