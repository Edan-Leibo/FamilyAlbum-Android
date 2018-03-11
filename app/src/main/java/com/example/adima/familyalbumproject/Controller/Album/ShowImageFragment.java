package com.example.adima.familyalbumproject.Controller.Album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adima.familyalbumproject.Model.Model;
import com.example.adima.familyalbumproject.R;

/**
 * Created by adima on 11/03/2018.
 */

public class ShowImageFragment extends Fragment {
    static String imageUrl;
   // private OnFragmentAlbumInteractionListener mListener;


    public ShowImageFragment() {
    }
/*
    public interface OnFragmentAlbumInteractionListener {

        void showAlbumsFragment();
        void showImageFragment(String imageUrl);

        void showCommentsFragment(String albumId);

    }
*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowImageFragment newInstance(String imageUrlF) {
        Log.d("TAG","in new instancee");
        imageUrl=imageUrlF;



        ShowImageFragment fragment = new ShowImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","in on create func");

        //setHasOptionsMenu(true);
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_back_to_albums:
                mListener.showAlbumsFragment();
                return true;
            case R.id.btn_comments:
                mListener.showCommentsFragment(albumId);
                return true;
            case R.id.fragment_album_btn_add:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Add A photo to an album");
                alertDialogBuilder.setMessage("Where do you want to take the photo from?\n");
                alertDialogBuilder.setPositiveButton("From camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        dispatchTakePictureIntent();
                    }
                });
                alertDialogBuilder.setNegativeButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        dispatchGetPictureFromGalleryIntent();
                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_image, container, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.large_image);
        Log.d("TAG","in on create view");
        Bitmap photoAvatar = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.photo_avatar);
        imageView.setImageBitmap(photoAvatar);
        //final Image img = imageList.get(position);
        imageView.setTag(imageUrl);
        Log.d("TAG","the iMAGE url is"+imageUrl);

        if (imageUrl!= null && !imageUrl.isEmpty() && !imageUrl.equals("")) {
            Log.d("TAG","the iMAGE url is"+imageUrl);
            Model.instance().getImage(imageUrl, new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    Log.d("TAG","GOT THE IMAGE");
                    String tagUrl = imageView.getTag().toString();
                    if (tagUrl.equals(imageUrl)) {
                        imageView.setImageBitmap(image);

                    }
                }

                @Override
                public void onFail() {

                }
            });
        }
        return view;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentAlbumInteractionListener) {
            mListener = (OnFragmentAlbumInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
       // Bundle args = getArguments();
        //albumId = args.getString("albumIdAlbumFragment", "");

    }

    @Override
    public void onDetach() {
        super.onDetach();

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








}
