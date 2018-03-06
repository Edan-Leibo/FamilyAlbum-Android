package com.example.adima.familyalbumproject.Controller.Albums;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Album.Model.AlbumsListViewModel;
import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.MyApplication;
import com.example.adima.familyalbumproject.R;

import java.util.LinkedList;
import java.util.List;

import Model.Firebase.FirebaseAuthentication;
import Model.Model;

import static android.content.Context.MODE_PRIVATE;


public class AlbumsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    ListView albumsListView;

    private static List<Album> albumList = new LinkedList<>();
    AlbumListAdapter adapter;
    ProgressBar progressBar;
    private static String familySerial;
    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";

    private AlbumsListViewModel albumListViewModel;

    public AlbumsFragment() {
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
        //getFamilyID
        familySerial = Model.instance().getFamilySerialFromSharedPrefrences("familyInfo",FAMILY_SERIAL);
        Bundle args = new Bundle();
        args.putString(FAMILY_SERIAL, familySerial);
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
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ListView list = view.findViewById(R.id.albums_list);
        adapter = new AlbumListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MyApplication.getMyContext(), i, Toast.LENGTH_SHORT).show();
            }
        });


        Button buttonAdd = (Button) view.findViewById(R.id.btn_add_album);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showCreateAlbumFragment();
            }
        });


        Button buttonGetSerial = (Button) view.findViewById(R.id.btn_get_family_serial);
        buttonGetSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("This is your family serial\nShare it with your other family members:\n" + familySerial);
                alertDialogBuilder.setNeutralButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        if (familySerial == "NONE") {
            buttonAdd.setEnabled(false);
            buttonGetSerial.setEnabled(false);
            Toast.makeText(MyApplication.getMyContext(), "You are not connected to any family yet", Toast.LENGTH_SHORT).show();
        } else {
            buttonAdd.setEnabled(true);
            buttonGetSerial.setEnabled(true);
        }

        view.findViewById(R.id.btn_join_family).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Please enter the family's serial number\n");
                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                alertDialogBuilder.setView(input);
                alertDialogBuilder.setNeutralButton("Connect me", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String serial = input.getText().toString();
                        Model.instance().isFamilyExist(serial, new Model.IsFamilyExistCallback() {
                            @Override
                            public void onComplete(boolean exist) {
                                if (exist == true) {
                                    //albumListViewModel.init(familySerial);
                                    //adapter.notifyDataSetChanged();
                                    //albumListViewModel.getAlbumList().removeObserver(getLifecycle().getCurrentState());
                                    Model.instance().writeToSharedPreferences("familyInfo", FAMILY_SERIAL, serial);
                                    ((MainActivity) getActivity()).showAlbumsFragment();

                                } else {
                                    Toast.makeText(MyApplication.getMyContext(), "Serial does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancel () {

                            }
                        });

                        }
                    });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //Toast.makeText(MyApplication.getMyContext(), "You are not connected to a family", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_create_family).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity) getActivity()).showLoginFragment();
               // ((MainActivity) getActivity()).showCommentsFragment("-L6qPSRjtlvsIpxwffsp");
                Model.instance().addNewFamily(new Model.GetKeyListener() {
                    @Override
                    public void onCompletion(String success) {
                        if(success==null){
                            Toast.makeText(MyApplication.getMyContext(), "Creation of family album failed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            familySerial=success;
                            Toast.makeText(MyApplication.getMyContext(), "New family album created", Toast.LENGTH_SHORT).show();
                            Model.instance().writeToSharedPreferences("familyInfo", FAMILY_SERIAL,success);
                            ((MainActivity) getActivity()).showAlbumsFragment();
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.btn_albums_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuthentication.signOut();
                progressBar.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showLoginFragment();
            }
        });

        progressBar = view.findViewById(R.id.album_list_progressbar);
        progressBar.setVisibility(View.GONE);

        /*if (familySerial.equals("NONE")){
            Toast.makeText(MyApplication.getMyContext(), "You are not connected to a family", Toast.LENGTH_SHORT).show();
        }*/
        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Bundle args = getArguments();
        familySerial = args.getString(FAMILY_SERIAL, "NONE");

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
    public interface OnFragmentInteractionListener {
        void onItemSelected(Album album);
    }


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
