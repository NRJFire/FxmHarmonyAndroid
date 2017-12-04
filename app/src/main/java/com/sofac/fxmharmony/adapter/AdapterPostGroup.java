package com.sofac.fxmharmony.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.ConverterHTML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PART_POST;

public class AdapterPostGroup extends RecyclerView.Adapter<AdapterPostGroup.ViewHolder> {
    private ArrayList<PostDTO> postDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;


    public AdapterPostGroup(Context context, ArrayList<PostDTO> postDTOArrayList) {
        this.postDTOArrayList = postDTOArrayList;
        this.ctx = context;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder view, int position) {

        PostDTO postDTO = postDTOArrayList.get(position);
        ArrayList<String> listImage = new ArrayList<>();

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);///////////////

        Uri uri = Uri.parse(BASE_URL + Constants.PART_AVATAR + postDTO.getAvatar());

        Glide.with(ctx)
                .load(uri)
                .override(AppMethods.getPxFromDp(50, ctx), AppMethods.getPxFromDp(50, ctx))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .into(view.avatar);

        view.titleItemPost.setText(postDTO.getName());
        view.dateItemPost.setText(postDTO.getDate().toString());
        view.dateItemPost.setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(postDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        if (postDTO.getBody_original() != null)
            view.messageItemPost.setText(ConverterHTML.fromHTML(postDTO.getBody_original()));

        //FILES
        if (postDTO.getDocs().size() > 0) {
            view.linearLayoutFiles.setVisibility(View.VISIBLE);
            view.linearLayoutFiles.removeAllViews();
            View fileItemView = inflater.inflate(R.layout.item_preview_post_file, null);
            TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
            if (postDTO.getDocs().size() == 1) {
                textView.setText("" + postDTO.getDocs().get(0) + "");
            } else if (postDTO.getDocs().size() > 1) {
                textView.setText(postDTO.getDocs().get(0) + " and " + (postDTO.getDocs().size()-1) + " files");
            }
            view.linearLayoutFiles.addView(fileItemView, lParams);
        } else {
            view.linearLayoutFiles.setVisibility(View.GONE);
        }

        //PHOTO CAROUSEL
        view.recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        if (postDTO.getImages().size() > 0) {
            view.recyclerView.setVisibility(View.VISIBLE);
            for (String imageName : postDTO.getImages()) {
                listImage.add(BASE_URL + PART_POST + imageName);
            }
            view.recyclerView.setAdapter(new AdapterGalleryGroup(listImage));
        } else {
            view.recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return postDTOArrayList.size();
    }

    // CLASS VIEW HOLDER

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutFiles;
        ImageView avatar;
        TextView titleItemPost;
        TextView dateItemPost;
        TextView messageItemPost;
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.idImageCarousel);
            avatar = (ImageView) view.findViewById(R.id.idAvatarPostItem);
            linearLayoutFiles = (LinearLayout) view.findViewById(R.id.idListFilesPostItem);
            titleItemPost = (TextView) view.findViewById(R.id.idTitleItemPost);
            dateItemPost = (TextView) view.findViewById(R.id.idDateItemPost);
            messageItemPost = (TextView) view.findViewById(R.id.idMessageItemPost);
        }
    }

}
