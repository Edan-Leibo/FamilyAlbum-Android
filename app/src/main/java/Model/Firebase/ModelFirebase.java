package Model.Firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Album.Model.AlbumFirebase;
import com.example.adima.familyalbumproject.Comment.Model.Comment;
import com.example.adima.familyalbumproject.Comment.Model.CommentFirebase;
import com.example.adima.familyalbumproject.Entities.Image;
import com.example.adima.familyalbumproject.FamiliesModel.FamiliesFirebase;
import com.example.adima.familyalbumproject.ImageUrl.Model.ImageFirebase;
import com.example.adima.familyalbumproject.User.User;
import com.example.adima.familyalbumproject.User.UserFirebase;
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
import java.util.LinkedList;
import java.util.List;

import Model.Model;

/**
 * Created by adima on 01/03/2018.
 */

public class ModelFirebase {

   public DatabaseFirebase databaseFirebase;

    public ModelFirebase(){
    this.databaseFirebase = new DatabaseFirebase();

    }



    public void addComment(String albumId, Comment comment, final OnCreation listener){
        CommentFirebase.addComment(albumId, comment, new CommentFirebase.OnCreationComment() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });

    }

    public void addImage(String albumId, Image image, final OnCreation listener){
        ImageFirebase.addImage(albumId, image, new ImageFirebase.OnCreationImage() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }
    public interface GetAllAlbumsAndObserveCallback {
        void onComplete(List<Album> list);
        void onCancel();
    }
    public interface GetKeyListener{
        public void onCompletion(String success);
    }






    /*
    public interface GetAllImageUrlsAndObserveCallback{
        void onComplete(List<String> list);
        void onCancel();
    }

    public void getAllImageUrlsAndObserve(final GetAllImageUrlsAndObserveCallback){

    }

    */


    public void getUserImageUrl(final Model.GetKeyListener listener){
        UserFirebase.getUserImageUrl(new GetKeyListener() {
            @Override
            public void onCompletion(String success) {
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

    public void addAlbum(Album album, String serialNumber, final OnCreation listener){
        //this.databaseFirebase.addAlbum(album);
        AlbumFirebase.addAlbum(album, serialNumber, new AlbumFirebase.OnCreationAlbum() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });


    }

        public void addNewFamily(final Model.GetKeyListener listener) {

           FamiliesFirebase.addFamily(new FamiliesFirebase.GetKeyListener() {
                @Override
                public void onCompletion(String success) {
                    listener.onCompletion(success);
                }
            });
        }



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


    public void getAllAlbumsAndObserve(final GetAllAlbumsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums");
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Album> list = new LinkedList<Album>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Album album = snap.getValue(Album.class);
                    Log.d("TAG","got the data in Album repository"+album.name);
                    Log.d("TAG","got the data in Album repository"+album.location);
                    Log.d("TAG","got the data in Album repository"+album.serialNumber);

                    Log.d("TAG","got the data in Album repository"+album.date);
                    list.add(album);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }



    public void getAlbums(DatabaseFirebase.GetAlbumsListener tag) {
        this.databaseFirebase.getAlbums(new DatabaseFirebase.GetAlbumsListener() {
            @Override
            public void onComplete(List<Album> studentList) {
                Log.d("TAG", "all the albums are in model fire base" );

            }
        });
    }

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


    public void removeAlbum(String albumId,String serialNumber){
        AlbumFirebase.removeAlbum(albumId,serialNumber);
    }

    public  void removeComment(String albumId,String commentId) {
        CommentFirebase.removeComment(albumId,commentId);
    }

    public  void removeFamily(String serialNumber) {
        FamiliesFirebase.removeFamily(serialNumber);
    }
    public  void removeImage(String albumId,String imageId) {
        ImageFirebase.removeImage(albumId,imageId);
    }
    public void addUserProfilePicture(User user, final OnCreation listener){
        UserFirebase.addUserProfilePicture(user, new UserFirebase.OnCreationUser() {
            @Override
            public void onCompletion(boolean success) {
                listener.onCompletion(success);
            }
        });
    }






    }
