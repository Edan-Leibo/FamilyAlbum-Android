package Model.Firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.adima.familyalbumproject.Album.Model.Album;
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

    public void addAlbum(Album album){
        this.databaseFirebase.addAlbum(album);


    }

    public interface GetAllAlbumsAndObserveCallback {
        void onComplete(List<Album> list);
        void onCancel();
    }

    //work
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



}
