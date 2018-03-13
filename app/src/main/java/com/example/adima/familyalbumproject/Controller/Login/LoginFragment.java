package com.example.adima.familyalbumproject.Controller.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.Model.Model.Authentication;
import com.example.adima.familyalbumproject.R;

/**
 * Created by adima on 03/03/2018.
 */

public class LoginFragment extends Fragment {
    private OnFragmentLoginInteractionListener mListener;
    ProgressBar progressBar;
    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";

    public interface OnFragmentLoginInteractionListener {
        void showAlbumsFragment();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = view.findViewById(R.id.login_progressbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText email = view.findViewById(R.id.editText_email);
        final EditText password = view.findViewById(R.id.editText_password);

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().length() < 6) {
                    Toast.makeText(MyApplication.getMyContext(), "Password must have minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email.getText())) {
                    Toast.makeText(MyApplication.getMyContext(), "Please insert a correct Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Authentication.loginUser(email.getText().toString(), password.getText().toString(), new Authentication.loginUserCallBack() {

                    @Override
                    public void onLogin(boolean t) {
                        if (t == true) {
                            Toast.makeText(MyApplication.getMyContext(), "You have been successfully logged in", Toast.LENGTH_SHORT).show();
                            mListener.showAlbumsFragment();
                        } else {
                            Toast.makeText(MyApplication.getMyContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        view.findViewById(R.id.createNewAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().length() < 6) {
                    Toast.makeText(MyApplication.getMyContext(), "Password must have minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email.getText())) {
                    Toast.makeText(MyApplication.getMyContext(), "Please insert a correct Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Authentication.registerUser(email.getText().toString(), password.getText().toString(), new Authentication.regUserCallBack() {
                    @Override
                    public void onRegistration(boolean t) {
                        if (t == true) {
                            Toast.makeText(MyApplication.getMyContext(), "You have been successfully registered", Toast.LENGTH_SHORT).show();
                            mListener.showAlbumsFragment();
                        } else {
                            Toast.makeText(MyApplication.getMyContext(), "Registration failed\nPlease try different Email or password", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentLoginInteractionListener) {
            mListener = (OnFragmentLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
