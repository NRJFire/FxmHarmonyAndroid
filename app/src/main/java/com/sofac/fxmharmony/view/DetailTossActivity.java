package com.sofac.fxmharmony.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.TOSS_ID;

public class DetailTossActivity extends BaseActivity {


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
    @BindView(R.id.textViewStatus)
    TextView textViewStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toss);
        ButterKnife.bind(this);

        progressBar.showView();
        new Connection<TossDTO>().getToss(getIntent().getStringExtra(TOSS_ID), (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                fillingFieldsView(answerServerResponse.getDataTransferObject());
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

    private void setListModel(ArrayList<TossMessageDTO> tossMessageDTOS) {
        adapterTossMessages = new AdapterTossMessages(tossMessageDTOS);
        recyclerViewMessage.setAdapter(adapterTossMessages);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setModel(TossDTO tossDTO) {
        Timber.e(tossDTO.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }


    private void changeStatus(String statusToss) {
        switch (statusToss) {
            case "closed":
                textViewStatus.setText("c\nl\no\ns\ne\nd");
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorRed));
                break;
            case "open":
                textViewStatus.setText("o\np\ne\nn");
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorGreen));
                break;
            case "pause":
                textViewStatus.setText("p\na\nu\ns\ne");
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorPurple));
                break;
            case "process":
                textViewStatus.setText("p\nr\no\nc\ne\ns\ns");
                viewRightStatus.setBackgroundColor(getResources().getColor(R.color.ColorYellow));
                break;
        }
    }
}
