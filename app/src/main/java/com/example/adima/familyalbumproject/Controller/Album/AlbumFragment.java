package com.example.adima.familyalbumproject.Controller.Album;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Model.Entities.Image.Image;
import com.example.adima.familyalbumproject.Model.Entities.Image.ImagesUrlListViewModel;
import com.example.adima.familyalbumproject.Model.Firebase.FirebaseAuthentication;
import com.example.adima.familyalbumproject.Model.Model.Model;
import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by adima on 03/03/2018.
 */

public class AlbumFragment extends Fragment {

    private OnFragmentAlbumInteractionListener mListener;
    private String albumId;
    List<Image> imageList = new LinkedList<>();
    ImageGridViewAdapter adapter;
    ProgressBar progressBar;
    private ImagesUrlListViewModel imagesListViewModel;

    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int PICK_IMAGE = 1;

    public AlbumFragment() {
    }

    public interface OnFragmentAlbumInteractionListener {

        void showAlbumsFragment();
        void showImageFragment(String imageUrl,String albumId);

        void showCommentsFragment(String albumId);

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
        setHasOptionsMenu(true);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);
        progressBar = view.findViewById(R.id.album_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        GridView grid = view.findViewById(R.id.gridview);
        adapter = new ImageGridViewAdapter();
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Image image = imageList.get(i);
                Log.d("TAG","THE IMAGE URL IN THE ALBUM:"+image.getImageUrl());
                ;
                mListener.showImageFragment(image.getImageUrl(),albumId);

            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Image image = imageList.get(i);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Delete the photo?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressBar.setVisibility(View.VISIBLE);
                                Model.instance().removeImage(image, new Model.OnRemove() {
                                    @Override
                                    public void onCompletion(boolean success) {
                                        if (success == true) {
                                            Toast.makeText(MyApplication.getMyContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MyApplication.getMyContext(), "could not delete the image", Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });

        progressBar.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAlbumInteractionListener) {
            mListener = (OnFragmentAlbumInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Bundle args = getArguments();
        albumId = args.getString("albumIdAlbumFragment", "");

        imagesListViewModel = ViewModelProviders.of(this).get(ImagesUrlListViewModel.class);
        imagesListViewModel.init(albumId);
        imagesListViewModel.getImagesList().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                if(images!=null&&images.size()>0){
                    if(albumId.equals(images.get(0).getAlbumId())){
                        imageList = images;
                    }


                }
                else if (images.size()==0||images==null){
                    imageList=images;

                }
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Model.instance().removeAllObserversFromImages();
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

    private void dispatchGetPictureFromGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(getContext().getPackageManager()) != null) {
            this.startActivityForResult(pickPhoto, PICK_IMAGE);
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap imageBitmap = null;
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageBitmap = BitmapFactory.decodeFile(picturePath);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
        if (imageBitmap != null) {
            final String uniqueName = FirebaseAuthentication.getUserEmail() + getUniqueId();
            Model.instance().saveImage(imageBitmap, uniqueName, new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    Image image = new Image();
                    image.setImageUrl(url);
                    image.setName(uniqueName);
                    image.setAlbumId(albumId);

                    Model.instance().addImage(albumId, image, new Model.OnCreation() {
                        @Override
                        public void onCompletion(boolean success) {
                            if (success) {
                                Toast.makeText(MyApplication.getMyContext(), "Photo was added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyApplication.getMyContext(), "Failed to add photo to album", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void fail() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyApplication.getMyContext(), "Failed to add the photo", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MyApplication.getMyContext(), "Photo was not added", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUniqueId() {
        Calendar cc = Calendar.getInstance();
        Date date = cc.getTime();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format2.format(date);
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
            Bitmap photoAvatar = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.photo_avatar);
            imageView.setImageBitmap(photoAvatar);
            final ProgressBar smallProgressBar = (ProgressBar) convertView.findViewById(R.id.image_cell_progressBar);
            final Image img = imageList.get(position);
            imageView.setTag(img.getImageUrl());
            if (img.getImageUrl() != null && !img.getImageUrl().isEmpty() && !img.getImageUrl().equals("")) {
                smallProgressBar.setVisibility(View.VISIBLE);
                Model.instance().getImage(img.getImageUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(img.getImageUrl())) {
                            imageView.setImageBitmap(image);
                            smallProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {
                        smallProgressBar.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }
    }
}
