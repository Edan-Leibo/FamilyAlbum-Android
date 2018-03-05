package com.example.adima.familyalbumproject.Controller.Album;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.Entities.Image;
import com.example.adima.familyalbumproject.ImageUrl.Model.ImagesUrlListViewModel;
import com.example.adima.familyalbumproject.R;

import java.util.LinkedList;
import java.util.List;

import Model.Model;

/**
 * Created by adima on 03/03/2018.
 */

public class AlbumFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String albumId;
    List<Image> imageList = new LinkedList<>();
    ImageGridViewAdapter adapter;
    ProgressBar progressBar;
    private ImagesUrlListViewModel imagesListViewModel;

    public AlbumFragment() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String albumId) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString("albumIdAlbumFragment", albumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAlbumFragment(albumId);
            }
        });
        GridView grid = view.findViewById(R.id.gridview);
        adapter = new ImageGridViewAdapter();
        grid.setAdapter(adapter);
        //TODO: add page progress bar
        //progressBar = view.findViewById(R.id.image_cell_progressBar);
        //progressBar.setVisibility(View.GONE);
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAlbumsFragment();
            }
        });
        view.findViewById(R.id.btn_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity) getActivity()).showCommentsFragment(album);
            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
        Bundle args = getArguments();
        albumId = args.getString("albumIdAlbumFragment", "");

        imagesListViewModel = ViewModelProviders.of(this).get(ImagesUrlListViewModel.class);
        imagesListViewModel.init(albumId);
        imagesListViewModel.getImagesList().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                imageList = images;
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onItemSelected(Image image);
    }

    class ImageGridViewAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.image_cell, null);
            }

            final ImageView imageView = (ImageView) convertView.findViewById(R.id.cell_image);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.image_cell_progressBar);
            final Image img = imageList.get(position);
            imageView.setTag(img.getImageUrl());
            if (img.getImageUrl() != null && !img.getImageUrl().isEmpty() && !img.getImageUrl().equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                Model.instance().getImage(img.getImageUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(img.getImageUrl())) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }
    }
}
