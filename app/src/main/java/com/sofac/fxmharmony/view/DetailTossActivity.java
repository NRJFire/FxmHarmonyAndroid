package com.sofac.fxmharmony.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTossMessages;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.dto.TossMessageDTO;
import com.sofac.fxmharmony.server.Connection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.TOSS_ID;

public class DetailTossActivity extends BaseActivity {

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
    @BindView(R.id.slideView)
    ConstraintLayout slideView;
    @BindView(R.id.dim)
    FrameLayout dim;
    @BindView(R.id.filesConstraintLayout)
    ConstraintLayout filesConstraintLayout;
    @BindView(R.id.commentsConstraintLayout)
    ConstraintLayout commentsConstraintLayout;
    @BindView(R.id.textViewBodyMessage)
    TextView textViewBodyMessage;
    @BindView(R.id.imageViewAvatar)
    ImageView imageViewAvatar;
    @BindView(R.id.textViewDateMessage)
    TextView textViewDateMessage;

    private TossDTO tossDTO;
    private AdapterTossMessages adapterTossMessages;
    private SlideUp slideUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toss);
        ButterKnife.bind(this);
        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadingFilesFromServer();
    }

    public void loadingFilesFromServer() {
        progressBar.showView();
        new Connection<TossDTO>().getToss(getIntent().getStringExtra(TOSS_ID), (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                initializationView(answerServerResponse.getDataTransferObject());
            } else {
                showToast(getResources().getString(R.string.errorServer));
            }
            progressBar.dismissView();
        });
    }

    public void initializationView(TossDTO updatedTossDTO) {
        slideUp = new SlideUpBuilder(slideView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {

                        }
                    }
                })
                .withGesturesEnabled(true)
                .build();

        this.tossDTO = updatedTossDTO;
        setModelToss(tossDTO);
        setModelListMessagesInView(tossDTO.getMessages());
    }

    private void setModelListMessagesInView(ArrayList<TossMessageDTO> tossMessageDTOS) {
        adapterTossMessages = new AdapterTossMessages(tossMessageDTOS);
        adapterTossMessages.setItemClickListener((view, position) -> {
            switch (view.getId()) {
                case R.id.buttonFiles:
                    showFilesOfThisMessage(tossMessageDTOS.get(position));
                    break;
                case R.id.buttonComments:
                    showCommentsOfThisMessage(tossMessageDTOS.get(position));
                    break;
            }
        });
        recyclerViewMessage.setAdapter(adapterTossMessages);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewMessage.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewMessage, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                slideUp.show();
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
    }

    public void showFilesOfThisMessage(TossMessageDTO tossMessageDTO) {
        showFilesViewSlideUp();
        setModelMessageForView(tossMessageDTO);
    }


    public void showCommentsOfThisMessage(TossMessageDTO tossMessageDTO) {
        showCommentsViewSlideUp();
        setModelMessageForView(tossMessageDTO);
    }

    public void showFilesViewSlideUp() {
        slideUp.show();
        filesConstraintLayout.setVisibility(View.VISIBLE);
        commentsConstraintLayout.setVisibility(View.GONE);
    }

    public void showCommentsViewSlideUp() {
        slideUp.show();
        commentsConstraintLayout.setVisibility(View.VISIBLE);
        filesConstraintLayout.setVisibility(View.GONE);
    }

    private void setModelMessageForView(TossMessageDTO messageDTO) {
        Uri uri = Uri.parse(BASE_URL + Constants.PART_AVATAR + messageDTO.getAvatar());
        Glide.with(this)
                .load(uri)
                .override(150, 150)
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imageViewAvatar);

        textViewBodyMessage.setText(messageDTO.getBody());
        textViewDateMessage.setText(messageDTO.getDate());
    }

    private void setModelToss(TossDTO tossDTO) {
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

    //Close sliding up
    @OnClick(R.id.buttonCloseSlide)
    public void onViewClicked() {
        slideUp.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toss_detail, menu);
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

}
