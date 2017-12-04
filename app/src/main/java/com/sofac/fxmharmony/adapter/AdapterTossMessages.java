package com.sofac.fxmharmony.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.TossMessageDTO;
import com.sofac.fxmharmony.util.AppMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PART_AVATAR;

/**
 * Created by Maxim on 29.11.2017.
 */

public class AdapterTossMessages extends RecyclerView.Adapter<AdapterTossMessages.TossViewHolder> {


    private ArrayList<TossMessageDTO> listMessages;

    public AdapterTossMessages(ArrayList<TossMessageDTO> tossMessageDTOS) {
        this.listMessages = tossMessageDTOS;
    }

    @Override
    public TossViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toss_message, parent, false);
        return new TossViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TossViewHolder holder, int position) {
        holder.setModel(listMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    class TossViewHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.textViewBodyMessage)
        TextView textViewBodyMessage;
        @BindView(R.id.buttonFiles)
        Button buttonFiles;
        @BindView(R.id.buttonComments)
        Button buttonComments;
        @BindView(R.id.imageViewAvatar)
        ImageView imageViewAvatar;
        @BindView(R.id.textViewAuthorMessage)
        TextView textViewAuthorMessage;
        @BindView(R.id.textViewDateTime)
        TextView textViewDateTime;

        TossViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setModel(TossMessageDTO messageDTO) {
            textViewBodyMessage.setText(messageDTO.getBody());
            textViewAuthorMessage.setText(messageDTO.getName());
            textViewDateTime.setText(messageDTO.getDate());


            Uri uri = Uri.parse(BASE_URL + Constants.PART_AVATAR + messageDTO.getAvatar());
            Timber.e(uri.toString());

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