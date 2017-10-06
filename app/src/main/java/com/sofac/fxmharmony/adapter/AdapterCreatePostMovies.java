package com.sofac.fxmharmony.adapter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;
import java.util.ArrayList;

/**
 * Created by Maxim on 04.10.2017.
 */

public class AdapterCreatePostMovies extends RecyclerView.Adapter<AdapterCreatePostMovies.ViewHolder> {

    private ArrayList<Uri> uriArrayList;
    private ClickListener itemClickListener;

    public interface ClickListener {
        void onMyClick(View view, int position);
    }

    public AdapterCreatePostMovies(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public void setItemClickListener(ClickListener myClickListener) {
        this.itemClickListener = myClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_movie_create_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //holder.linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(holder.itemView.getContext())
                .load(uriArrayList.get(position))
                .override(AppMethods.getPxFromDp(50, holder.itemView.getContext()), AppMethods.getPxFromDp(50, holder.itemView.getContext()))
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private FrameLayout imageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageMoviePreview);
            imageButton = (FrameLayout) itemView.findViewById(R.id.idButtonDeleting);
            itemView.setOnClickListener(this);
            imageButton.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onMyClick(v, getAdapterPosition());
        }
    }
}
