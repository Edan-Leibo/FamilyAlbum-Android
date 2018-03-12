package com.example.adima.familyalbumproject.Model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Entities.Image.Image;
import com.example.adima.familyalbumproject.Model.Firebase.ModelFirebase;
import com.example.adima.familyalbumproject.Model.SQL.AlbumRepository;
import com.example.adima.familyalbumproject.Model.SQL.CommentRepository;
import com.example.adima.familyalbumproject.Model.SQL.ImageRepository;
import com.example.adima.familyalbumproject.Controller.Start.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adima on 01/03/2018.
 */



public class Model {

    private static Model instance = new Model();

    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";

    ModelFirebase modelFirebase;


    private Model() {
        this.modelFirebase  = new ModelFirebase();

    }

    public static Model instance() {
        return instance;
    }


    public String getFamilySerialFromSharedPrefrences(String familyInfo, String familySerial) {
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("familyInfo", MODE_PRIVATE);
        familySerial = ref.getString(FAMILY_SERIAL, "NONE");
        return familySerial;
    }


    public void writeToSharedPreferences(String name, String key, String value) {
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences(name,MODE_PRIVATE);
        SharedPreferences.Editor ed = ref.edit();
        ed.putString(key, value);
        ed.commit();
    }


    public interface GetAllAlbumsAndObserveCallback {
        void onComplete(List<Album> list);

        void onCancel();
    }

    public interface IsFamilyExistCallback{
        void onComplete(boolean exist);

        void onCancel();
    }


/*
checks if family exist in firebase
 */
    public void isFamilyExist(final String serialNumber,final IsFamilyExistCallback callback){
       modelFirebase.isFamilyExist(serialNumber,new ModelFirebase.IsFamilyExistCallback() {
           @Override
           public void onComplete(boolean exist) {
               callback.onComplete(exist);
           }

           @Override
           public void onCancel() {
               callback.onCancel();
           }
       });

    }


    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                Log.d("TAG","the file name in saveImage model"+fileName);
                saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }



    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }
    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        Log.d("TAG","LOOK FOR IMAGEURL"+url);
        String fileName = URLUtil.guessFileName(url, null, null);
        Bitmap image = loadImageFromFile(fileName);

        if (image != null){
            Log.d("TAG","the image already exist in local so i dont look in firebase");
            Log.d("TAG","getImage from local success " + fileName);
            listener.onSuccess(image);
        }else {
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    String fileName = URLUtil.guessFileName(url, null, null);
                    Log.d("TAG","getImage from FB success " + fileName);
                    saveImageToFile(image,fileName);
                    listener.onSuccess(image);
                }

                @Override
                public void onFail() {
                    Log.d("TAG","getImage from FB fail ");
                    listener.onFail();
                }
            });

        }
    }


    ////// SAVE FILES TO LOCAL STORAGE ///////

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            Log.d("TAG","THE IMAGE FILE NAME IN SAVE IMAGE TO FILE FUNC IS:"+imageFileName);
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
    }




    public void addComment(String albumId, Comment comment, final OnCreation listener){
        this.modelFirebase.addComment(albumId, comment, new ModelFirebase.OnCreation() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }

    public void addImage(String albumId, Image image, final OnCreation listener){
        this.modelFirebase.addImage(albumId, image, new ModelFirebase.OnCreation() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });

    }

    public interface GetKeyListener{
        public void onCompletion(String success);
    }

    public void addNewFamily(final GetKeyListener listener){
        modelFirebase.addNewFamily(new GetKeyListener() {
            @Override
            public void onCompletion(String success) {
                listener.onCompletion(success);
            }
        });


    }

    public interface OnCreation{
        public void onCompletion(boolean success);
    }

    public void addAlbum(Album album, String serialNumber, final OnCreation listener) {
        //this.databaseFirebase.addAlbum(album);
        modelFirebase.addAlbum(album, serialNumber, new ModelFirebase.OnCreation() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });


    }



public interface OnRemove{
    public void onCompletion(boolean success);
}

public void removeComment(final Comment comment,final OnRemove listener){

    modelFirebase.removeComment(comment, new ModelFirebase.OnRemove() {
        @Override
        public void onCompletion(boolean success) {
            if (success){
                CommentRepository.instance.removeFromLocalDb(comment);
            }
            listener.onCompletion(success);
        }
    });


}

    public void removeAlbum(final Album album,String serialNumber, final OnRemove listener) {
        AlbumRepository.instance.removeFromLocalDb(album);

        modelFirebase.removeAlbum(album, new ModelFirebase.OnRemove() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);

            }
        });
    }

    public void removeImage(final Image image, final OnRemove listener){
        ImageRepository.instance.removeFromLocalDb(image);

        modelFirebase.removeImage(image, new ModelFirebase.OnRemove() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);

            }
        });

    }




    }