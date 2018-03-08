package Model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.adima.familyalbumproject.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import Model.Entities.Album.Album;
import Model.Entities.Comment.Comment;
import Model.Entities.Image.Image;
import Model.Entities.User.User;
import Model.Firebase.DatabaseFirebase;
import Model.Firebase.ModelFirebase;
import Model.SQL.AlbumRepository;
import Model.SQL.CommentRepository;
import Model.SQL.ImageRepository;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adima on 01/03/2018.
 */



public class Model {

    private static Model instance = new Model();

    private final static String FAMILY_SERIAL = "FAMILY_SERIAL";

    ModelFirebase modelFirebase;
    //ModelSql modelSql;


    private Model() {
        this.modelFirebase  = new ModelFirebase();
        //this.modelSql = new ModelSql(MyApplication.getMyContext());



    }

    public static Model instance() {
        return instance;
    }
/*
    public void addAlbum(Album album) {
        modelFirebase.addAlbum(album);

    }
*/
    public void getAlbums() {
       this.modelFirebase.databaseFirebase.getAlbums(new DatabaseFirebase.GetAlbumsListener() {
            @Override
            public void onComplete(List<Album> albums) {
                    Log.d("TAG", "got all the albums in the model");
            }
        });



    }

    public String getFamilySerialFromSharedPrefrences(String familyInfo, String familySerial) {
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("familyInfo", MODE_PRIVATE);
        familySerial = ref.getString(FAMILY_SERIAL, "NONE");
        return familySerial;
    }

    public void addUserProfilePicture(User user, final OnCreation listener){
        this.modelFirebase.addUserProfilePicture(user, new ModelFirebase.OnCreation() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
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


    public void getAllAlbumsAndObserve(final GetAllAlbumsAndObserveCallback callback) {
        //return StudentSql.getAllStudents(modelSql.getReadableDatabase());
        modelFirebase.getAllAlbumsAndObserve(new ModelFirebase.GetAllAlbumsAndObserveCallback() {
            @Override
            public void onComplete(List<Album> list) {
                callback.onComplete(list);
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
        String fileName = URLUtil.guessFileName(url, null, null);
        Bitmap image = loadImageFromFile(fileName);

        if (image != null){
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

    public void getUserProfilePicture(final GetKeyListener listener){
        modelFirebase.getUserImageUrl(new GetKeyListener() {
            @Override
            public void onCompletion(String success) {
                listener.onCompletion(success);

            }
        });
    }
/*
    public void removeAlbum(String albumId,String serialNumber){
        this.modelFirebase.removeAlbum(albumId,serialNumber);
        AppLocalStore.db.albumDao().delete();



    }
*/
public interface OnRemove{
    public void onCompletion(boolean success);
}

public void removeComment(final Comment comment,final OnRemove listener){
    CommentRepository.instance.removeFromLocalDb(comment);

    modelFirebase.removeComment(comment, new ModelFirebase.OnRemove() {
        @Override
        public void onCompletion(boolean success) {
            listener.onCompletion(success);
        }
    });


}

    public void removeAlbum(final Album album,String serialNumber, final OnRemove listener) {
        AlbumRepository.instance.removeFromLocalDb(album,serialNumber);

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



/*
    modelFirebase.removeAlbum(album, new ModelFirebase.OnRemove() {
        @Override
        public void onCompletion(boolean success) {
            listener.onCompletion(success);
            MyDelete delete= new MyDelete();
            delete.execute(album);

        }
    });

*/


    }
/*
    public  void removeComment(String albumId,String commentId) {
        this.modelFirebase.removeComment(albumId,commentId);
    }
*/

    public  void removeFamily(String serialNumber) {
        this.modelFirebase.removeFamily(serialNumber);
    }
/*
    public  void removeImage(String albumId,String imageId) {
        this.modelFirebase.removeImage(albumId,imageId);
    }

*/


    }