package com.example.adima.familyalbumproject.Controller.Album;

import android.app.DatePickerDialog;
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

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.MyApplication;
import com.example.adima.familyalbumproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Model.Model;


public class CreateAlbumFragment extends Fragment {
    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";
    private EditText albumDate;
    private Calendar myCalendar;

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


        final String familySerial = Model.instance().getFamilySerialFromSharedPrefrences("familyInfo",FAMILY_SERIAL);

        Album album = new Album();

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).showAlbumsFragment();
            }
        });

        view.findViewById(R.id.btn_create_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Album album = new Album();
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
                        ((MainActivity) getActivity()).showAlbumFragment();
                    }
                });
            }
        });

        progressBar.setVisibility(View.GONE);

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
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        albumDate.setText(sdf.format(myCalendar.getTime()));
    }
}
