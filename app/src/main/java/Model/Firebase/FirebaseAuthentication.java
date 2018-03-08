package Model.Firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication {
    private static FirebaseAuth mAuth;
    private static String  userEmail; //The string of the current user which logged in to the app
    private static FirebaseUser user;


    public static interface loginUserCallBack{
        void onLogin(boolean t);
    }
    //validate the value entered by the user when the user is logging in to the app
    public static void loginUser(final String email, String password, final loginUserCallBack callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    user = mAuth.getCurrentUser();
                    userEmail = user.getEmail(); //set the User's Email
                    callback.onLogin(true);


                } else {
                    // If sign in fails
                    callback.onLogin(false);

                }
            }
        });
    }


    public interface regUserCallBack{
        void onRegistration(boolean t);
    }

    public static void registerUser(final String email, String password, final regUserCallBack callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    user = mAuth.getCurrentUser();
                    userEmail = user.getEmail(); //Set the users Email Address

                    callback.onRegistration(true);

                } else {
                    // If sign in fails
                    callback.onRegistration(false);
                }
            }
        });
    }

    /*
     */
    public static void signOut() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut(); //sign out of the FireBase
        user = null;
        userEmail = null;
    }

    /*
    returns if the user is signed in or not
     */
    public static boolean isSignedIn() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            return true;
        } else {
            return false;
        }
    }

    /*
    Return the User email
     */
    public static String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String user) {
        userEmail = user;
    }

}
