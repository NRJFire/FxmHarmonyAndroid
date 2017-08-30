package com.sofac.fxmharmony.view.customView;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;


public class PhotoVideoItemWithCancel extends RelativeLayout {

    private Uri pictureUri;

    private List<String> photoVideoList;
    private List<Uri> photoVideoListToSend;


    public PhotoVideoItemWithCancel(Context context, Uri uri, List<String> photoVideoList, List<Uri> photoVideoListToSend, boolean isRemoteVideo) {
        super(context);

        this.pictureUri = uri;
        this.photoVideoList = photoVideoList;
        this.photoVideoListToSend = photoVideoListToSend;


        int padding = AppMethods.getPxFromDp(5, context);
        int height = AppMethods.getPxFromDp(100, context);
        int width = AppMethods.getPxFromDp(100, context);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        this.setLayoutParams(layoutParams);
        this.setPadding(padding, padding, padding, padding);

        ImageView imagePhoto = new ImageView(context);

        final Button cancelButton = new Button(context);

        this.addView(imagePhoto);
        this.addView(cancelButton);

        RelativeLayout.LayoutParams imagePhotoLayoutParams = (RelativeLayout.LayoutParams) imagePhoto.getLayoutParams();
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, cancelButton.getId());
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_END, cancelButton.getId());
        imagePhotoLayoutParams.height = AppMethods.getPxFromDp(height, context);
        imagePhotoLayoutParams.width = AppMethods.getPxFromDp(width, context);


        RelativeLayout.LayoutParams cancelButtonLayoutParams = (RelativeLayout.LayoutParams) cancelButton.getLayoutParams();
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        cancelButtonLayoutParams.width = AppMethods.getPxFromDp(30, context);
        cancelButtonLayoutParams.height = AppMethods.getPxFromDp(30, context);

        if (!isRemoteVideo) {
            Glide.with(context)
                    .load(uri)
                    .error(R.drawable.no_video)
                    .override(height, height)
                    .centerCrop()
                    .into(imagePhoto);
        } else {

            String thumbnailURL = Constants.BASE_URL + Constants.GET_POST_thumbnails_END_URL + uri + Constants.POINT_PNG;
            Glide.with(context)
                    .load(thumbnailURL)
                    .error(R.drawable.no_video)
                    .override(height, height)
                    .centerCrop()
                    .into(imagePhoto);

        }


        cancelButton.setBackground(context.getResources().getDrawable((R.drawable.remove_symbol)));

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Iterator<String> photoVideoListIterator = PhotoVideoItemWithCancel.this.photoVideoList.iterator();
                while (photoVideoListIterator.hasNext()) {
                    String photoVideoURL = Constants.BASE_URL + Constants.GET_POST_FILES_END_URL + photoVideoListIterator.next();

                    if (photoVideoURL.equals(pictureUri.toString())) {
                        photoVideoListIterator.remove();
                    }
                }


                Iterator<Uri> photoVideoListToSendIterator = PhotoVideoItemWithCancel.this.photoVideoListToSend.iterator();
                while (photoVideoListToSendIterator.hasNext()) {
                    Uri photoVideoUri = photoVideoListToSendIterator.next();
                    if (photoVideoUri.equals(pictureUri)) {
                        photoVideoListToSendIterator.remove();

                    }
                }


                LinearLayoutGallery parent = (LinearLayoutGallery) PhotoVideoItemWithCancel.this.getParent();
                parent.removeView(PhotoVideoItemWithCancel.this);


            }
        });

    }


}
