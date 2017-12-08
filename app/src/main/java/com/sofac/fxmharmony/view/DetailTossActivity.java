package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTossComments;
import com.sofac.fxmharmony.adapter.AdapterTossMessages;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.dto.TossCommentDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.dto.TossMessageDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.FileLoadingListener;
import com.sofac.fxmharmony.util.FileLoadingTask;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.LINK_IMAGE;
import static com.sofac.fxmharmony.Constants.LINK_VIDEO;
import static com.sofac.fxmharmony.Constants.NAME_IMAGE;
import static com.sofac.fxmharmony.Constants.NAME_VIDEO;
import static com.sofac.fxmharmony.Constants.PART_POST;
import static com.sofac.fxmharmony.Constants.PART_TOSS;
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

    @BindView(R.id.filesListContainer)
    LinearLayout filesConstraintLayout;

    @BindView(R.id.commentsConstraintLayout)
    ConstraintLayout commentsConstraintLayout;
    @BindView(R.id.textViewBodyMessage)
    TextView textViewBodyMessage;
    @BindView(R.id.imageViewAvatar)
    ImageView imageViewAvatar;
    @BindView(R.id.textViewDateMessage)
    TextView textViewDateMessage;
    @BindView(R.id.recyclerViewComments)
    RecyclerView recyclerViewComments;
    @BindView(R.id.mainConstrainLayout)
    ConstraintLayout mainConstrainLayout;
    @BindView(R.id.buttonSendComment)
    Button buttonSendComment;
    @BindView(R.id.editTextComment)
    EditText editTextComment;
    @BindView(R.id.idListPhotos)
    LinearLayout linearLayoutPhotos;
    @BindView(R.id.idListVideos)
    LinearLayout linearLayoutVideos;
    @BindView(R.id.idListFiles)
    LinearLayout linearLayoutFiles;

    private String idMessage = "1";
    private TossDTO tossDTO;
    private AdapterTossMessages adapterTossMessages;
    private SlideUp slideUp;
    AdapterTossComments adapterTossComments;


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
                showToast(getResources().getString(R.string.errorServerConnection));
            }
            progressBar.dismissView();
        });
    }

    public void initializationView(TossDTO updatedTossDTO) {
        mainConstrainLayout.setVisibility(View.VISIBLE);
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
        setTossInHeader(tossDTO);
        setListMessagesInView(tossDTO.getMessages());
    }

    private void setTossInHeader(TossDTO tossDTO) {
        viewTextTitle.setText(tossDTO.getTitle());
        viewTextDate.setText(tossDTO.getDate());
        viewNamesFrom.setText(tossDTO.getName());
        viewNamesTo.setText(getNamesResponsible(tossDTO.getResponsible()));
        changeStatus(tossDTO.getStatus());
    }

    private void setListMessagesInView(ArrayList<TossMessageDTO> tossMessageDTOS) {
        adapterTossMessages = new AdapterTossMessages(tossMessageDTOS);
        adapterTossMessages.setItemClickListener((view, position) -> {
            idMessage = tossMessageDTOS.get(position).getId();
            switch (view.getId()) {
                case R.id.buttonFiles:
                    createViewFiles(tossMessageDTOS.get(position));
                    break;
                case R.id.buttonComments:
                    createViewComments(tossMessageDTOS.get(position));
                    break;
            }
        });
        recyclerViewMessage.setAdapter(adapterTossMessages);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createViewFiles(TossMessageDTO tossMessageDTO) {
        showSlideUpFiles();
        setMessageInHeaderSlideUp(tossMessageDTO);
        fillingFiles(tossMessageDTO);
    }

    public void createViewComments(TossMessageDTO tossMessageDTO) {
        showSlideUpComments();
        setMessageInHeaderSlideUp(tossMessageDTO);
        adapterTossComments = new AdapterTossComments(tossMessageDTO.getComments());
        recyclerViewComments.setAdapter(adapterTossComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showSlideUpFiles() {
        slideUp.show();
        filesConstraintLayout.setVisibility(View.VISIBLE);
        commentsConstraintLayout.setVisibility(View.GONE);
    }

    public void showSlideUpComments() {
        slideUp.show();
        commentsConstraintLayout.setVisibility(View.VISIBLE);
        filesConstraintLayout.setVisibility(View.GONE);
    }

    private void setMessageInHeaderSlideUp(TossMessageDTO messageDTO) {
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
                break;
            case R.id.createNewMessage:
                startActivityForResult(new Intent(this, CreateTossMessageActivity.class), 1);
                break;
        }
        return true;
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

    @OnClick({R.id.buttonCloseSlide, R.id.buttonSendComment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonCloseSlide:
                slideUp.hide();
                linearLayoutPhotos.removeAllViews();
                linearLayoutVideos.removeAllViews();
                linearLayoutFiles.removeAllViews();
                break;
            case R.id.buttonSendComment:
                requestNewComment();
                break;
        }
    }

    public void requestNewComment() {
        if (!editTextComment.getText().toString().isEmpty()) {
            progressBar.showView();
            new Connection<TossCommentDTO>().addTossComment(
                    new SenderContainerDTO(
                            appPreference.getID(),
                            idMessage,
                            editTextComment.getText().toString(),
                            tossDTO.getId()),
                    (isSuccess, answerServerResponse) -> {
                        if (isSuccess) {
                            slideUp.hide();
                            loadingFilesFromServer();
                        } else {
                            showToast(getResources().getString(R.string.errorServerConnection));
                        }
                    });
        }
    }

    public void fillingFiles(TossMessageDTO messageDTO) {

        Timber.e(messageDTO.getImages().toString());
        Timber.e(messageDTO.getMovies().toString());
        Timber.e(messageDTO.getDocs().toString());

        // START LIST IMAGE
        if (messageDTO.getImages().size() > 0) {
            for (final String imageName : messageDTO.getImages()) {

                View photoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_photo, null);
                ImageView imageView = (ImageView) photoItemView.findViewById(R.id.idItemPhoto);

                Glide.with(this)
                        .load(BASE_URL + PART_TOSS + imageName)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(imageView);
                linearLayoutPhotos.addView(photoItemView);
                photoItemView.setOnClickListener(v13 -> {
                    Intent intentPhoto = new Intent(DetailTossActivity.this, PreviewPhotoActivity.class);
                    intentPhoto.putExtra(LINK_IMAGE, ("" + BASE_URL + PART_TOSS + imageName));
                    intentPhoto.putExtra(NAME_IMAGE, ("" + imageName));
                    startActivity(intentPhoto);
                });
            }

        } else {
            linearLayoutPhotos.setVisibility(View.INVISIBLE);
        }
        // START LIST IMAGE


        // START VIDEO
        if (messageDTO.getMovies().size() > 0) {
            for (final String videoName : messageDTO.getMovies()) {

                View videoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_video, null);
                ImageView imageVideoView = (ImageView) videoItemView.findViewById(R.id.idItemVideo);

                Uri uriVideo = Uri.parse(BASE_URL + PART_TOSS + videoName + ".png");
                Glide.with(this)
                        .load(uriVideo)
                        .error(R.drawable.no_video)
                        .placeholder(R.drawable.no_video)
                        .into(imageVideoView);
                linearLayoutVideos.addView(videoItemView);

                videoItemView.setOnClickListener(v12 -> {
                    Intent intentVideo = new Intent(DetailTossActivity.this, PreviewVideoActivity.class);
                    intentVideo.putExtra(LINK_VIDEO, ("" + BASE_URL + PART_TOSS + videoName));
                    intentVideo.putExtra(NAME_VIDEO, ("" + videoName));
                    startActivity(intentVideo);
                });
            }

        } else {
            linearLayoutVideos.setVisibility(View.INVISIBLE);
        }
        // END VIDEO


        // START FILES
        if (messageDTO.getDocs().size() > 0) {
            for (final String fileName : messageDTO.getDocs()) {
                View fileItemView = getLayoutInflater().inflate(R.layout.item_detail_post_file, null);
                TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
                textView.setText(fileName);

                fileItemView.setOnClickListener(v1 -> {
                    if (isStoragePermissionGranted()) {
                        new FileLoadingTask(
                                BASE_URL + PART_TOSS + fileName,
                                new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName),
                                new FileLoadingListener() {
                                    @Override
                                    public void onBegin() {
                                        Toast.makeText(DetailTossActivity.this, "Begin download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DetailTossActivity.this, "Successful download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Throwable cause) {
                                        Toast.makeText(DetailTossActivity.this, "Error download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onEnd() {

                                    }
                                }).execute();
                    }
                });
                linearLayoutFiles.addView(fileItemView);
            }
        } else {
            linearLayoutFiles.setVisibility(View.INVISIBLE);
        }

        // END FILES
    }
}
