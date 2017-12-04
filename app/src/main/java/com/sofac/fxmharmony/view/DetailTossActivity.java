package com.sofac.fxmharmony.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTossMessages;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.dto.TossMessageDTO;
import com.sofac.fxmharmony.server.Connection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sofac.fxmharmony.Constants.ONE_TOSS_MESSAGE_DATA;

public class DetailTossActivity extends BaseActivity {

    public Toolbar toolbar;
    private TossDTO tossDTO;
    private AdapterTossMessages adapterTossMessages;

    @BindView(R.id.viewRightStatus)
    View viewRightStatus;
    @BindView(R.id.viewTextTitle)
    TextView viewTextTitle;
    @BindView(R.id.viewTextDate)
    TextView viewTextDate;
    @BindView(R.id.viewNamesFrom)
    TextView viewNamesFrom;
    @BindView(R.id.viewNamesTo)
    TextView viewNamesTo;
    @BindView(R.id.recyclerViewMessage)
    RecyclerView recyclerViewMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toss);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar.showView();
        new Connection<TossDTO>().getToss(((TossDTO) getIntent().getSerializableExtra(ONE_TOSS_MESSAGE_DATA)).getId(), (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                fillingFieldsView(tossDTO);
            } else {
                showToast(getResources().getString(R.string.errorServer));
            }
            progressBar.dismissView();
        });
    }

    public void fillingFieldsView(TossDTO updatedTossDTO) {
        this.tossDTO = updatedTossDTO;
        setModel(tossDTO);
        setListModel(tossDTO.getMessages());
    }

    private void setListModel(ArrayList<TossMessageDTO> tossMessageDTOS){
        adapterTossMessages = new AdapterTossMessages(tossMessageDTOS);
        recyclerViewMessage.setAdapter(adapterTossMessages);
    }

    private void setModel(TossDTO tossDTO) {
        viewTextTitle.setText(tossDTO.getTitle());
        viewTextDate.setText(tossDTO.getDate());
        viewNamesFrom.setText(tossDTO.getName());
        viewNamesTo.setText(getNamesResponsible(tossDTO.getResponsible()));
        changeStatus(tossDTO.getStatus());
    }

    private String getNamesResponsible(ResponsibleUserDTO[] listUsers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ResponsibleUserDTO responsibleUser : listUsers) {
            stringBuilder.append(String.format("%s, ", responsibleUser.getName()));
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return stringBuilder.toString();
    }

    private void changeStatus(String statusToss) {
        switch (statusToss) {
            case "closed":
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorRed));
                break;
            case "open":
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorGreen));
                break;
            case "pause":
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorPurple));
                break;
            case "process":
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorYellow));
                break;
        }
    }
}
