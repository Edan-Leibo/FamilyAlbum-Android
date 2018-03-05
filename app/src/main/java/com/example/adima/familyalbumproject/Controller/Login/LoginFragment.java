package com.example.adima.familyalbumproject.Controller.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.MyApplication;
import com.example.adima.familyalbumproject.R;

import Model.Firebase.FirebaseAuthentication;

/**
 * Created by adima on 03/03/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.login).setOnClickListener(this);
        view.findViewById(R.id.createNewAccount).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).showAlbumsFragment();
        FirebaseAuthentication fb = new FirebaseAuthentication();
//        fb.registerUser("edan@gmail.com", "1234abcd", new FirebaseAuthentication.regUserCallBack() {
//            @Override
//            public void onRegistration(boolean t) {
//                Toast.makeText(MyApplication.getMyContext(), "Authentication "+t, Toast.LENGTH_SHORT).show();
//            }
//        });
        fb.loginUser("edan@gmail.com", "1234abcd", new FirebaseAuthentication.loginUserCallBack(){

            @Override
            public void onLogin(boolean t) {
                Toast.makeText(MyApplication.getMyContext(), "Authentication "+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
