package com.sofac.fxmharmony.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.TossCommentDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.BASE_URL;

/**
 * Created by Maxim on 29.11.2017.
 */

public class AdapterTossComments extends RecyclerView.Adapter<AdapterTossComments.TossViewHolder> {


    private ArrayList<TossCommentDTO> listComments;
    private ClickListener itemClickListener;

    public interface ClickListener {
        void onMyClick(View view, int position);
    }

    public AdapterTossComments(ArrayList<TossCommentDTO> tossCommentDTOS) {
        this.listComments = tossCommentDTOS;
    }

    public void setItemClickListener(ClickListener myClickListener) {
        this.itemClickListener = myClickListener;
    }

    @Override
    public TossViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toss_comment, parent, false);
        return new TossViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TossViewHolder holder, int position) {
        holder.setModel(listComments.get(position));
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    class TossViewHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.textViewDateTime)
        TextView textViewDateTime;
        @BindView(R.id.textViewBodyMessage)
        TextView textViewBodyMessage;
        @BindView(R.id.textViewAuthorMessage)
        TextView textViewAuthorMessage;
        @BindView(R.id.imageViewAvatar)
        ImageView imageViewAvatar;

        TossViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setModel(TossCommentDTO commentDTO) {
            textViewBodyMessage.setText(commentDTO.getBody());
            textViewAuthorMessage.setText(commentDTO.getName());
            textViewDateTime.setText(commentDTO.getDate());

            Uri uri = Uri.parse(BASE_URL + Constants.PART_AVATAR + commentDTO.getAvatar());

            Glide.with(view.getContext())
                    .load(uri)
                    .override(150, 150)
                    .error(R.drawable.no_avatar)
                    .placeholder(R.drawable.no_avatar)
                    .bitmapTransform(new CropCircleTransformation(view.getContext()))
                    .into(imageViewAvatar);
        }

    }
}