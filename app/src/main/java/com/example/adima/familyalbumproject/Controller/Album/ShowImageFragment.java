package com.example.adima.familyalbumproject.Controller.Album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    static String albumId;
   private OnFragmentShowImageInteractionListener mListener;


    public ShowImageFragment() {
    }

    public interface OnFragmentShowImageInteractionListener {
        void showAlbumFragment(String albumId);

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowImageFragment newInstance(String imageUrlF,String albumIdF) {
        Log.d("TAG","in new instancee");
        imageUrl=imageUrlF;
        albumId=albumIdF;
        ShowImageFragment fragment = new ShowImageFragment();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        args.putString("albumId",albumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.image_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_back_to_album:
                Log.d("TAG","THE ALBUM ID IS:"+albumId);
                mListener.showAlbumFragment(albumId);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

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
        if (context instanceof OnFragmentShowImageInteractionListener) {
            mListener = (OnFragmentShowImageInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Bundle args = getArguments();
        albumId = args.getString("albumId", "");
        imageUrl=args.getString("imageUrl","");
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
        mListener=null;

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
