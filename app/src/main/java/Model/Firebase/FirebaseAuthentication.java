package Model.Firebase;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Idan on 05/03/2018.
 */

public class FirebaseAuthentication {



    private FirebaseAuth mAuth;
    FirebaseUser user;
    String userName; //The string of the current user which logged in to the app
    //This string is needed in order to add Edit rights to the users posts



    //validate the value entered by the user when the user is logging in to the app
    public void loginUser(final String email, String password, Activity act, final FirebaseAuthentication.loginUserCallBack callback)  {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    user = mAuth.getCurrentUser();
                    userName=user.getEmail(); //set the User's Email
                    callback.onLogin(true);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(finalProject.getMyContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    callback.onLogin(false);

                }
            }
        });

    }

    //When the user first register to the app.
    //The user is entering his details and validate the email and password
    public void regUser(final String email, String password, Activity activity,final ModelUser.regUserCallBack callback)
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    user = mAuth.getCurrentUser();
                    userName=user.getEmail(); //Set the users Email Address
                    callback.onReg(true);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(finalProject.getMyContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    callback.onReg(false);
                }
            }
        });

    }

    /*
    When the users is finished using the App
    The user is singing out
     */
    public void signOut()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut(); //sign out of the FireBase
        user=null;
        userName=null;
    }

    /*
    return if the user is sign in
     */
    public boolean isSignIn()
    {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if( user!=null) {
            userName=user.getEmail();
            return true;
        }
        else
        {
            return false;
        }
    }

    /*
    Return the User Name
     */
    public String getUsername()
    {

        return userName;
    }


    /*
    Set the User Name
     */
    public void setUsername(String user)
    {
        userName=user;
    }

}