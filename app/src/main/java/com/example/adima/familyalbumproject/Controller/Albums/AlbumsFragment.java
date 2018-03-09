package com.example.adima.familyalbumproject.Controller.Albums;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adima.familyalbumproject.MyApplication;
import com.example.adima.familyalbumproject.R;

import java.util.LinkedList;
import java.util.List;

import Model.Entities.Album.Album;
import Model.Entities.Album.AlbumsListViewModel;
import Model.Entities.User.User;
import Model.Firebase.FirebaseAuthentication;
import Model.Model;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;



public class AlbumsFragment extends Fragment {
    private OnFragmentAlbumsInteractionListener mListener;
    ListView albumsListView;

    private static List<Album> albumList = new LinkedList<>();
    AlbumListAdapter adapter;
    ProgressBar progressBar;
    private static String familySerial;
    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int PICK_IMAGE = 1;
    //MenuItem  addAlbumItem;
    //MenuItem getSerialItem;
    //MenuItem getJoinItem;
    //MenuItem createItem;

    private AlbumsListViewModel albumListViewModel;

    public AlbumsFragment() {

    }

    public interface OnFragmentAlbumsInteractionListener {
        void showAlbumsFragment();
        void showLoginFragment();
        void showAlbumFragment(String albumId);
        void showCreateAlbumFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("familyInfo", MODE_PRIVATE);
        familySerial = ref.getString(FAMILY_SERIAL, "NONE");
        Bundle args = new Bundle();
        args.putString(FAMILY_SERIAL, familySerial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        familySerial = args.getString(FAMILY_SERIAL, "NONE");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.albums_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //getJoinItem=menu.findItem(R.id.btn_join_family);
        //createItem=menu.findItem(R.id.btn_create_family);
        //getSerialItem=menu.findItem(R.id.btn_get_family_serial);
        //addAlbumItem = menu.findItem(R.id.btn_add_album);

        if (familySerial == "NONE") {
            //addAlbumItem.setVisible(false);
            //getSerialItem.setVisible(false);
            //getJoinItem.setVisible(true);
            //createItem.setVisible(true);
            Toast.makeText(MyApplication.getMyContext(), "You are not connected to any family yet", Toast.LENGTH_SHORT).show();
        } else {
            //addAlbumItem.setVisible(true);
            //getSerialItem.setVisible(true);
            //getJoinItem.setVisible(false);
            //createItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_add_album:
                mListener.showCreateAlbumFragment();
                return true;
            case R.id.btn_get_family_serial:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("This is your family serial\nShare it with your other family members:\n" + familySerial);
                alertDialogBuilder.setNeutralButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.btn_edit_photo:
                AlertDialog.Builder alertDialogBuilderGetSerial = new AlertDialog.Builder(getContext());
                alertDialogBuilderGetSerial.setTitle("Get a photo");
                alertDialogBuilderGetSerial.setMessage("Where do you want to take the photo from?\n");
                alertDialogBuilderGetSerial.setPositiveButton("From camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        dispatchTakePictureIntent();
                    }
                });
                alertDialogBuilderGetSerial.setNegativeButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        dispatchGetPictureFromGalleryIntent();
                    }
                });
                alertDialogBuilderGetSerial.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialogGetSerial = alertDialogBuilderGetSerial.create();
                alertDialogGetSerial.show();
                return true;
            case R.id.btn_join_family:
                final AlertDialog.Builder alertDialogBuilderJoin = new AlertDialog.Builder(getContext());
                alertDialogBuilderJoin.setMessage("Please enter the family's serial number\n");
                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alertDialogBuilderJoin.setView(input);
                alertDialogBuilderJoin.setNeutralButton("Connect me", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String serial = input.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        Model.instance().isFamilyExist(serial, new Model.IsFamilyExistCallback() {
                            @Override
                            public void onComplete(boolean exist) {
                                if (exist) {
                                    Model.instance().writeToSharedPreferences("familyInfo", FAMILY_SERIAL, serial);
                                    mListener.showAlbumsFragment();

                                   // getJoinItem.setVisible(false);
                                   // createItem.setVisible(false);

                                } else {
                                    Toast.makeText(MyApplication.getMyContext(), "Serial does not exist", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancel() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                });
                AlertDialog alertDialogJoin = alertDialogBuilderJoin.create();
                alertDialogJoin.show();
                return true;
            case R.id.btn_create_family:
                progressBar.setVisibility(View.VISIBLE);
                Model.instance().addNewFamily(new Model.GetKeyListener() {
                    @Override
                    public void onCompletion(String success) {
                        if (success == null) {
                            Toast.makeText(MyApplication.getMyContext(), "Creation of family album failed", Toast.LENGTH_SHORT).show();
                        } else {
                            familySerial = success;
                            //addAlbumItem.setVisible(false);
                            Toast.makeText(MyApplication.getMyContext(), "New family was created", Toast.LENGTH_SHORT).show();
                            Model.instance().writeToSharedPreferences("familyInfo", FAMILY_SERIAL, success);
                            mListener.showAlbumsFragment();
                           // getJoinItem.setVisible(false);
                            //createItem.setVisible(false);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
                return true;
            case R.id.btn_albums_exit:
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuthentication.signOut();
                Model.instance().writeToSharedPreferences("familyInfo", FAMILY_SERIAL, "NONE");
                progressBar.setVisibility(View.GONE);
                //Model.instance().clearCache(albumList,familySerial);
                mListener.showLoginFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            Model.instance().saveImage(imageBitmap, FirebaseAuthentication.getUserEmail(), new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    User user = new User();
                    user.setEmailUser(FirebaseAuthentication.getUserEmail());
                    user.setImageUrl(url);
                    Model.instance().addUserProfilePicture(user, new Model.OnCreation() {
                        @Override
                        public void onCompletion(boolean success) {
                            if (success) {
                                Toast.makeText(MyApplication.getMyContext(), "Photo was saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyApplication.getMyContext(), "Failed to save photo", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void fail() {
                    Toast.makeText(MyApplication.getMyContext(), "Failed to save photo", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(MyApplication.getMyContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }




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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        progressBar = view.findViewById(R.id.album_list_progressbar);
        progressBar.setVisibility(View.VISIBLE);


        ListView list = view.findViewById(R.id.albums_list);
        adapter = new AlbumListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Album album = albumList.get(i);
                Log.d("TAG", "got item number:" + i);
                mListener.showAlbumFragment(album.getAlbumId());

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Album album = albumList.get(i);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Delete the album?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Model.instance().removeAlbum(album,familySerial, new Model.OnRemove() {
                                    @Override
                                    public void onCompletion(boolean success) {
                                        if (success==true){
                                            Toast.makeText(MyApplication.getMyContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MyApplication.getMyContext(), "could not delete the image", Toast.LENGTH_SHORT).show();
                                        }

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
                return  true;
            }
        });

        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAlbumsInteractionListener) {
            mListener = (OnFragmentAlbumsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.albumListViewModel = ViewModelProviders.of(this).get(AlbumsListViewModel.class);
        //familySerial="-L6pJ7h5JSIjz-WQctTl";
        this.albumListViewModel.init(familySerial);
        albumListViewModel.getAlbumList().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(@Nullable List<Album> albums) {
                //if (albums.size() > 0) {
                albumList = albums;
                if (adapter != null) adapter.notifyDataSetChanged();
                //} else {
                //
                //}
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



    class AlbumListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return albumList.size();
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
            if (convertView == null) {   //On creation
                convertView = inflater.inflate(R.layout.album_list_row, null);
            }

            TextView albumName = (TextView) convertView.findViewById(R.id.album_list_row_name);
            TextView albumLocation = (TextView) convertView.findViewById(R.id.album_list_row_location);
            TextView albumDate = (TextView) convertView.findViewById(R.id.album_list_row_date);

            final Album alb = albumList.get(position);
            albumName.setText(alb.getName());
            albumLocation.setText(alb.getLocation());
            albumDate.setText(alb.getDate());

            return convertView;
        }
    }

}
