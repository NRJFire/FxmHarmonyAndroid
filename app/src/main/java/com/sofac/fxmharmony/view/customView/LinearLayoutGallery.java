package com.sofac.fxmharmony.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LinearLayoutGallery extends LinearLayout {

    private TextView galleryName;
    private boolean isGalleryNameExist = false;

    public LinearLayoutGallery(Context context) {
        super(context);
        this.setVisibility(View.GONE);

    }

    public LinearLayoutGallery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setVisibility(View.GONE);

    }

    public LinearLayoutGallery(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setVisibility(View.GONE);

    }



    public void setGalleryView(TextView galleryName) {
        this.galleryName = galleryName;
        this.galleryName.setVisibility(View.GONE);
    /*    ViewGroup parent = (ViewGroup) this.getParent();
        parent.setVisibility(View.GONE);
*/
    }

    @Override
    public void addView(View child) {
        if (!isGalleryNameExist) {
        /*    ViewGroup parent = (ViewGroup) this.getParent();
            parent.setVisibility(View.VISIBLE);*/
            galleryName.setVisibility(View.VISIBLE);
            this.setVisibility(View.VISIBLE);
            isGalleryNameExist = true;
        }
        super.addView(child);
    }

    @Override
    public void removeView(View child) {
        super.removeView(child);
        if (this.getChildCount() == 0) {
            galleryName.setVisibility(View.GONE);
            isGalleryNameExist = false;
            this.setVisibility(View.GONE);
        /*    ViewGroup parent = (ViewGroup) this.getParent();
            parent.setVisibility(View.GONE);*/
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
