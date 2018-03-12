package com.example.adima.familyalbumproject.Model.Firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Entities.Album.AlbumFirebase;
import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Entities.Comment.CommentFirebase;
import com.example.adima.familyalbumproject.Model.Entities.Family.FamiliesFirebase;
import com.example.adima.familyalbumproject.Model.Entities.Image.Image;
import com.example.adima.familyalbumproject.Model.Entities.Image.ImageFirebase;
import com.example.adima.familyalbumproject.Model.Model.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by adima on 01/03/2018.
 */
/*
This class represnts The model interaction with firebase
 */
public class ModelFirebase {

    public ModelFirebase(){

    }

    /**
     * Add a new comment to an album
     * @param albumId
     * @param comment
     * @param listener
     */
    public void addComment(String albumId, Comment comment, final OnCreation listener){
        CommentFirebase.addComment(albumId, comment, new CommentFirebase.OnCreationComment() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });

    }

    /**
     * Add an image to an album
     * @param albumId
     * @param image
     * @param listener
     */
    public void addImage(String albumId, Image image, final OnCreation listener){
        ImageFirebase.addImage(albumId, image, new ImageFirebase.OnCreationImage() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }



    public interface IsFamilyExistCallback{
        void onComplete(boolean exist);
        void onCancel();
    }

    public interface OnCreation{
        public void onCompletion(boolean success);
    }

    /**
     * Add an album to a family
     * @param album
     * @param serialNumber
     * @param listener
     */
    public void addAlbum(Album album, String serialNumber, final OnCreation listener){
        //this.databaseFirebase.addAlbum(album);
        AlbumFirebase.addAlbum(album, serialNumber, new AlbumFirebase.OnCreationAlbum() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }

    /**
     * Add a new family
     * @param listener
     */
        public void addNewFamily(final Model.GetKeyListener listener) {

           FamiliesFirebase.addFamily(new FamiliesFirebase.GetKeyListener() {
                @Override
                public void onCompletion(String success) {
                    listener.onCompletion(success);
                }
            });
        }

    /**
     *  Checks if the serial number of the family exist
     * @param serialNumber
     * @param callback
     */
    public void isFamilyExist(final String serialNumber,final IsFamilyExistCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("families").child(serialNumber);
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() !=null) {
                    Log.d("TAG","the family exist");
                    callback.onComplete(true);
                }
                else {
                    Log.d("TAG","the family is not exist");

                    callback.onComplete(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });

    }


    /**
     * Save an image in firebase storage
     * @param imageBmp
     * @param name
     * @param listener
     */
    public void saveImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    /**
     * Get image from firebase
     * @param url
     * @param listener
     */
    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("TAGGGGG","the url is"+url);
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                listener.onFail();
            }
        });
    }
    public interface OnRemove{
        public void onCompletion(boolean success);
    }

    /**
     * Remove an album from firebase
     * @param album
     * @param listener
     */
    public void removeAlbum(Album album, final OnRemove listener){
        AlbumFirebase.removeAlbum(album, new OnRemove() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });

    }

    /**
     * Remove a comment from firebase
     * @param comment
     * @param listener
     */
    public  void removeComment(Comment comment, final OnRemove listener) {
        CommentFirebase.removeComment(comment, new OnRemove() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success );

            }
        });
    }

    /**
     * Remove an image from firebase
     * @param image
     * @param listener
     */
    public  void removeImage(Image image, final OnRemove listener) {
        ImageFirebase.removeImage(image, new OnRemove() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }


    }