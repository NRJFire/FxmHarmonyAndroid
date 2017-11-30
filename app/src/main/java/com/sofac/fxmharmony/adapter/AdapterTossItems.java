package com.sofac.fxmharmony.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.TossDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Maxim on 29.11.2017.
 */

public class AdapterTossItems extends RecyclerView.Adapter<AdapterTossItems.TossViewHolder> {

    private ArrayList<TossDTO> listTosses;

    public AdapterTossItems(ArrayList<TossDTO> tossDTOs){
        this.listTosses = tossDTOs;
    }

    @Override
    public TossViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toss, parent, false);
        return new TossViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TossViewHolder holder, int position) {
        holder.setModel(listTosses.get(position));
    }

    @Override
    public int getItemCount() {
        return listTosses.size();
    }

    class TossViewHolder extends RecyclerView.ViewHolder {
        View view;

        @BindView(R.id.viewTextTitle) TextView viewTextTitle;
        @BindView(R.id.viewTextDate) TextView viewTextDate;
        @BindView(R.id.viewNamesFrom) TextView viewNamesFrom;
        @BindView(R.id.viewNamesTo) TextView viewNamesTo;
        @BindView(R.id.viewRightStatus) View viewRightStatus;

        TossViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setModel(TossDTO tossDTO) {
            viewTextTitle.setText(tossDTO.getTitle());
            viewTextDate.setText(tossDTO.getDate());
            viewNamesFrom.setText(tossDTO.getName());
            viewNamesTo.setText(getNamesResponsible(tossDTO.getResponsible()));
            changeStatus(tossDTO.getStatus());
        }

        private String getNamesResponsible(ResponsibleUserDTO[] listUsers){
            StringBuilder stringBuilder = new StringBuilder();
            for(ResponsibleUserDTO responsibleUser : listUsers){
                stringBuilder.append(String.format("%s, ",responsibleUser.getName()));
            }
            stringBuilder.delete(stringBuilder.length()-3,stringBuilder.length()-1);
            return stringBuilder.toString();
        }

        private void changeStatus(String statusToss){
            switch (statusToss){
                case "closed":
                    viewRightStatus.setBackgroundColor(view.getResources().getColor(R.color.ColorRed));
                    break;
                case "open":
                    viewRightStatus.setBackgroundColor(view.getResources().getColor(R.color.ColorGreen));
                    break;
                case "pause":
                    viewRightStatus.setBackgroundColor(view.getResources().getColor(R.color.ColorPurple));
                    break;
                case "process":
                    viewRightStatus.setBackgroundColor(view.getResources().getColor(R.color.ColorYellow));
                    break;
            }
        }

    }
}
