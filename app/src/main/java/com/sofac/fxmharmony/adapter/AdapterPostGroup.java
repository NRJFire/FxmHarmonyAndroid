package com.sofac.fxmharmony.adapter;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.ConvertorHTML;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PART_URL_FILE_IMAGE_POST;


public class AdapterPostGroup extends RecyclerView.Adapter<AdapterPostGroup.ViewHolder> {
    private ArrayList<PostDTO> postDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;


    public AdapterPostGroup(Context context, ArrayList<PostDTO> postDTOArrayList) {
        Collections.sort(postDTOArrayList, new Comparator<PostDTO>() {
            public int compare(PostDTO o1, PostDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
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

        PostDTO postDTO = getPostDTO(position);
        ArrayList<String> listImage = new ArrayList<>();

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);///////////////

        Uri uri = Uri.parse(BASE_URL + Constants.PART_URL_FILE_AVATAR + postDTO.getPostUserAvatarImage());

        Glide.with(ctx)
                .load(uri)
                .override(AppMethods.getPxFromDp(50, ctx), AppMethods.getPxFromDp(50, ctx))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .into(view.avatar);


        view.titleItemPost.setText(postDTO.getUserName());
        view.dateItemPost.setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(postDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        if (postDTO.getPostTextOriginal() != null)  view.messageItemPost.setText(ConvertorHTML.fromHTML(postDTO.getPostTextOriginal()));

        if (null != postDTO.getLinksFile() && !"".equals(postDTO.getLinksFile()) && postDTO.getLinksFile().length() > 5) {
            view.linearLayoutFiles.setVisibility(View.VISIBLE);
            for (final String imageName : postDTO.getLinksFile().split(";#")) {

                View fileItemView = inflater.inflate(R.layout.item_preview_post_file, null);
                TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
                textView.setText(imageName);
                view.linearLayoutFiles.addView(fileItemView, lParams);
            }

        } else {
            view.linearLayoutFiles.setVisibility(View.INVISIBLE);
        }

        view.recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL,false));

        if (null != postDTO.getLinksImage() && !"".equals(postDTO.getLinksImage()) && postDTO.getLinksImage().length() > 5) {
            view.recyclerView.setVisibility(View.VISIBLE);
            for (String imageName : postDTO.getLinksImage().split(";#")) {
                listImage.add(BASE_URL + PART_URL_FILE_IMAGE_POST + imageName);
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

    public Object getItem(int position) {
        return postDTOArrayList.get(position);
    }

    PostDTO getPostDTO(int position) {
        return ((PostDTO) getItem(position));
    }



    /// CLASS VIEW HOLDER

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutFiles;
        ImageView avatar;
        TextView titleItemPost;
        TextView dateItemPost;
        TextView messageItemPost;
        //DiscreteScrollView discreteScrollView;
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            //discreteScrollView = (DiscreteScrollView) view.findViewById(R.id.idImageCarousel);
            recyclerView = (RecyclerView) view.findViewById(R.id.idImageCarousel);
            avatar = (ImageView) view.findViewById(R.id.idAvatarPostItem);
            linearLayoutFiles = (LinearLayout) view.findViewById(R.id.idListFilesPostItem);
            titleItemPost = (TextView) view.findViewById(R.id.idTitleItemPost);
            dateItemPost = (TextView) view.findViewById(R.id.idDateItemPost);
            messageItemPost = (TextView) view.findViewById(R.id.idMessageItemPost);
        }
    }

}
