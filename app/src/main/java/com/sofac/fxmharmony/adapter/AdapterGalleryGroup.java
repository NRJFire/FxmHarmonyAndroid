package com.sofac.fxmharmony.adapter;

import android.app.Fragment;
import android.graphics.Point;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.R;

import java.util.ArrayList;

import static android.R.attr.width;

/**
 * Created by Maxim on 21.07.2017.
 */

public class AdapterGalleryGroup extends RecyclerView.Adapter<AdapterGalleryGroup.ViewHolder> {

    private ArrayList<String> data;

    public AdapterGalleryGroup(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_gallery_post, parent, false);
        return new ViewHolder(v);
    }

    private static final int MINI_THUMB_SIZE = 600;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemCount() == 1) {

//            float wight = holder.itemView.getContext().getResources().getDimension(R.dimen.dp250);
//            int widthInt = (int) wight
            holder.linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .placeholder(R.drawable.no_image)
                    .override(MINI_THUMB_SIZE,MINI_THUMB_SIZE)
                    .error(R.drawable.no_image)
                    .into(holder.image);

        } else if (getItemCount() == 2) {

            float height = holder.itemView.getContext().getResources().getDimension(R.dimen.dp200);
            int heightInt = (int) height;
            double width = heightInt * 1.5;
            int widthInt = (int) width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthInt, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,6,0);
            holder.linearLayout.setLayoutParams(layoutParams);
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .placeholder(R.drawable.no_image)
                    .override(MINI_THUMB_SIZE,MINI_THUMB_SIZE)
                    .error(R.drawable.no_image)
                    .into(holder.image);

        } else if (getItemCount() == 3){

            float height = holder.itemView.getContext().getResources().getDimension(R.dimen.dp200);
            int heightInt = (int) height;
            double width = heightInt * 1.2;
            int widthInt = (int) width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthInt, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,6,0);
            holder.linearLayout.setLayoutParams(layoutParams);
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .placeholder(R.drawable.no_image)
                    .override(MINI_THUMB_SIZE,MINI_THUMB_SIZE)
                    .error(R.drawable.no_image)
                    .into(holder.image);
        } else {
            float height = holder.itemView.getContext().getResources().getDimension(R.dimen.dp160);
            int heightInt = (int) height;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(heightInt, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,6,0);
            holder.linearLayout.setLayoutParams(layoutParams);
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .placeholder(R.drawable.no_image)
                    .override(MINI_THUMB_SIZE,MINI_THUMB_SIZE)
                    .error(R.drawable.no_image)
                    .into(holder.image);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.idFrameGallery);
            image = (ImageView) itemView.findViewById(R.id.imageViewGallery);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
