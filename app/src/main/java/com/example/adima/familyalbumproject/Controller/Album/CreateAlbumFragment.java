package com.example.adima.familyalbumproject.Controller.Album;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.Model.Model.Authentication;
import com.example.adima.familyalbumproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Model.Model;


public class CreateAlbumFragment extends Fragment {
    private OnFragmentCreateAlbumInteractionListener mListener;

    private EditText albumDate;
    private Calendar myCalendar;

    public interface OnFragmentCreateAlbumInteractionListener{
        void showAlbumsFragment();
    }

    public static CreateAlbumFragment newInstance() {
        CreateAlbumFragment fragment = new CreateAlbumFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProgressBar progressBar = view.findViewById(R.id.createNewAlbumProgressBar);
        final EditText albumName = (EditText) view.findViewById(R.id.createAlbum_album_name);
        albumDate= (EditText) view.findViewById(R.id.createAlbum_album_date);
        final EditText albumLocation= (EditText) view.findViewById(R.id.createAlbum_album_location);


        final String familySerial = Model.instance().getFamilySerialFromSharedPrefrences("familyInfo", Authentication.getUserEmail());

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showAlbumsFragment();
            }
        });

        view.findViewById(R.id.btn_create_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((albumName.getText().toString().equals("")) || (albumDate.getText().toString().equals("")) || albumLocation.getText().toString().equals(""))){
                    Toast.makeText(MyApplication.getMyContext(), "You must fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                final Album album = new Album();
                album.setSerialNumber(familySerial);
                album.setName(albumName.getText().toString());
                album.setDate(albumDate.getText().toString());
                album.setLocation(albumLocation.getText().toString());
                Model.instance().addAlbum(album,familySerial, new Model.OnCreation() {
                    @Override
                    public void onCompletion(boolean success) {
                        if(success==true){
                            Toast.makeText(MyApplication.getMyContext(), "Album was added successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MyApplication.getMyContext(), "Could not add album", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                        mListener.showAlbumsFragment();
                    }
                });
            }
        });


        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        albumDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCreateAlbumInteractionListener) {
            mListener = (OnFragmentCreateAlbumInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        albumDate.setText(sdf.format(myCalendar.getTime()));
    }
}
