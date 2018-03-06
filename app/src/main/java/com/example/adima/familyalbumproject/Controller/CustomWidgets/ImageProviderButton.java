package com.example.adima.familyalbumproject.Controller.CustomWidgets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;

import com.example.adima.familyalbumproject.Controller.MainActivity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class ImageProviderButton extends android.support.v7.widget.AppCompatButton {
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int PICK_IMAGE = 1;

    public ImageProviderButton(Context context) {
        super(context);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Get a photo");
                alertDialogBuilder.setMessage("Where do you want to take the photo from?\n");
                alertDialogBuilder.setPositiveButton("From camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                });
                alertDialogBuilder.setNeutralButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchGetPictureFromGalleryIntent();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private void dispatchGetPictureFromGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(getContext().getPackageManager()) != null) {
            ((MainActivity)getContext()).startActivityForResult(pickPhoto, PICK_IMAGE);
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            ((MainActivity)getContext()).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public ImageProviderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageProviderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public CharSequence getAccessibilityClassName() {
        return super.getAccessibilityClassName();
    }

    @Override
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        return super.onResolvePointerIcon(event, pointerIndex);
    }


}
