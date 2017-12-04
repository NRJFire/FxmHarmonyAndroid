package com.sofac.fxmharmony.view;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCommentsGroup;
import com.sofac.fxmharmony.dto.CommentDTO;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.AppPreference;
import com.sofac.fxmharmony.util.ConvertorHTML;
import com.sofac.fxmharmony.util.FileLoadingListener;
import com.sofac.fxmharmony.util.FileLoadingTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.LINK_IMAGE;
import static com.sofac.fxmharmony.Constants.LINK_VIDEO;
import static com.sofac.fxmharmony.Constants.NAME_IMAGE;
import static com.sofac.fxmharmony.Constants.NAME_VIDEO;
import static com.sofac.fxmharmony.Constants.PART_AVATAR;
import static com.sofac.fxmharmony.Constants.PART_POST;
import static com.sofac.fxmharmony.Constants.POST_ID;

public class DetailPostActivity extends BaseActivity {

    public static Long idComment = 0L;
    public static Boolean isCreatingComment = true;
    public static CommentDTO commentDTO;
    public Intent intentChangePost;

    Button buttonSend;
    Intent intent;
    View headerView;
    ArrayList<CommentDTO> arrayListComments;
    ListView listViewComments;
    PostDTO postDTO;
    EditText editTextComment;
    AdapterCommentsGroup adapterCommentsGroup;
    ClipboardManager clipboardManager;
    ClipData clipData;
    LinearLayout linearLayout;
    Parcelable state;
    TextView messageTextView;
    TextView buttonTranslatePushMessage;
    TextView messageTranslatePushMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        setTitle(getString(R.string.FXM_group));


        intentChangePost = new Intent(this, ChangePost.class);

        Long id_post = getIntent().getLongExtra(POST_ID, 1);

        postDTO = PostDTO.findById(PostDTO.class, id_post);

        if (state != null) {
            listViewComments.onRestoreInstanceState(state);
        }
        linearLayout = new LinearLayout(this);

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        buttonSend = (Button) findViewById(R.id.sendComment);
        editTextComment = (EditText) findViewById(R.id.edit_text_comment);
        listViewComments = (ListView) findViewById(R.id.idListViewComments);

        listViewComments.setOnItemClickListener((parent, itemClicked, position, id) -> {
            if (arrayListComments != null) {
                if (position > 0) {
                    editTextComment.append(arrayListComments.get(position - 1).getName() + ", ");
                }
            }
        });

        listViewComments.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position > 0) {
                commentDTO = arrayListComments.get(position - 1);
                if (commentDTO.getId() != null) {
                    DetailPostActivity.idComment = commentDTO.getId();
                    if (commentDTO.getUser_id().equals(appPreference.getID())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPostActivity.this);
                        builder.setItems(R.array.choice_double_click_post, (dialog, which) -> {

                            switch (which) {
                                case 0: //Edit
                                    editTextComment.setText("");
                                    editTextComment.append(commentDTO.getBody());
                                    isCreatingComment = false;
                                    break;
                                case 1: //Delete
                                    progressBar.showView();
                                    new Connection<String>().deleteComment(commentDTO, (isSuccess, answerServerResponse) -> {
                                        if (isSuccess) {
                                            updateListView();
                                            Toast.makeText(DetailPostActivity.this, R.string.comment_was_delete, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(DetailPostActivity.this, "Error deleting!", Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.dismissView();
                                    });
                                    break;
                            }
                        });
                        builder.show();
                    }
                } else {
                    Toast.makeText(DetailPostActivity.this, R.string.problem_with_ID_comment, Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        });

        buttonSend.setOnClickListener(v -> {
            if (!(editTextComment.getText().toString()).isEmpty()) {
                progressBar.showView();
                if (isCreatingComment) { //Создание коментария

                    new Connection<String>().createComment(new CommentDTO(1L, appPreference.getID(), postDTO.getId(), editTextComment.getText().toString(), "", "", ""), (isSuccess, answerServerResponse) -> {
                        if (isSuccess) {
                            updateListView();
                            editTextComment.setText("");
                        } else {
                            Toast.makeText(DetailPostActivity.this, R.string.create_comment_error, Toast.LENGTH_SHORT).show();
                        }
                        progressBar.dismissView();
                    });

                } else { // Редактирование коментария
                    commentDTO.setBody(editTextComment.getText().toString());
                    new Connection<String>().updateComment(commentDTO, (isSuccess, answerServerResponse) -> {
                        if (isSuccess) {
                            updateListView();
                            editTextComment.setText("");
                            isCreatingComment = true;
                        } else {
                            Toast.makeText(DetailPostActivity.this, R.string.edit_comment_error, Toast.LENGTH_SHORT).show();
                        }
                        progressBar.dismissView();
                    });
                }

            } else {
                Toast.makeText(DetailPostActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
            }
        });

        initialHeaderPost();
        updateListView();
    }


    public void initialHeaderPost() {
        if (postDTO != null) {
            listViewComments.addHeaderView(createHeaderPost(), null, false);
        }
    }

    public View createHeaderPost() {
        headerView = createPostView(postDTO.getName(), new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()), postDTO.getBody_original());
        Spinner spinnerLanguage = (Spinner) headerView.findViewById(R.id.spinner_language);

        ArrayList<String> stringsSpinnerLanguage = new ArrayList<>();
        if (postDTO.getBody_en() != null && !postDTO.getBody_en().isEmpty())
            stringsSpinnerLanguage.add(getString(R.string.english_spinner));
        if (postDTO.getBody_ko() != null && !postDTO.getBody_ko().isEmpty())
            stringsSpinnerLanguage.add(getString(R.string.korean_spinner));
        if (postDTO.getBody_ru() != null && !postDTO.getBody_ru().isEmpty())
            stringsSpinnerLanguage.add(getString(R.string.russian_spinner));
        if (!stringsSpinnerLanguage.isEmpty())
            stringsSpinnerLanguage.add(0, getString(R.string.original_spinner));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, stringsSpinnerLanguage);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguage.setAdapter(adapter);
        if (stringsSpinnerLanguage.isEmpty()) spinnerLanguage.setVisibility(View.INVISIBLE);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem().toString().equals(getString(R.string.original_spinner))) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_original()));

                } else if (parent.getSelectedItem().toString().equals(getString(R.string.english_spinner))) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_en()));

                } else if (parent.getSelectedItem().toString().equals(getString(R.string.korean_spinner))) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_ko()));

                } else if (parent.getSelectedItem().toString().equals(getString(R.string.russian_spinner))) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_ru()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return headerView;
    }

    View createPostView(String name, String date, String message) {
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = getLayoutInflater().inflate(R.layout.post_view_detail, null);

        // START AVATAR
        Uri uri = Uri.parse(BASE_URL + PART_AVATAR + postDTO.getAvatar());
        ImageView avatar = (ImageView) v.findViewById(R.id.idAvatarPost);
        Glide.with(this)
                .load(uri)
                .override(AppMethods.getPxFromDp(40, this), AppMethods.getPxFromDp(40, this))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(avatar);
        // END AVATAR

        ((TextView) v.findViewById(R.id.idNamePost)).setText(name);
        ((TextView) v.findViewById(R.id.idDatePost)).setText(date);
        messageTextView = (TextView) v.findViewById(R.id.idMessagePost);
        buttonTranslatePushMessage = (TextView) v.findViewById(R.id.idTranslatePush);
        messageTranslatePushMessage = (TextView) v.findViewById(R.id.messageTranslatePushMessage);

        // START LIST IMAGE
        LinearLayout linearLayoutPhotos = (LinearLayout) v.findViewById(R.id.idListPhotos);

        if (postDTO.getImages().size() > 0) {
            for (final String imageName : postDTO.getImages()) {

                View photoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_photo, null);
                ImageView imageView = (ImageView) photoItemView.findViewById(R.id.idItemPhoto);

                Glide.with(this)
                        .load(BASE_URL + PART_POST + imageName)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(imageView);
                linearLayoutPhotos.addView(photoItemView, lParams);
                photoItemView.setOnClickListener(v13 -> {
                    Intent intentPhoto = new Intent(DetailPostActivity.this, PreviewPhotoActivity.class);
                    intentPhoto.putExtra(LINK_IMAGE, ("" + BASE_URL + PART_POST + imageName));
                    intentPhoto.putExtra(NAME_IMAGE, ("" + imageName));
                    startActivity(intentPhoto);
                });
            }

        } else {
            linearLayoutPhotos.setVisibility(View.INVISIBLE);
        }
        // START LIST IMAGE


        // START VIDEO
        LinearLayout linearLayoutVideos = (LinearLayout) v.findViewById(R.id.idListVideos);

        if (postDTO.getMovies().size() > 0) {
            for (final String videoName : postDTO.getMovies()) {

                View videoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_video, null);
                ImageView imageVideoView = (ImageView) videoItemView.findViewById(R.id.idItemVideo);

                Uri uriVideo = Uri.parse(BASE_URL + PART_POST + videoName + ".png");
                Glide.with(this)
                        .load(uriVideo)
                        .error(R.drawable.no_video)
                        .placeholder(R.drawable.no_video)
                        .into(imageVideoView);
                linearLayoutVideos.addView(videoItemView, lParams);

                videoItemView.setOnClickListener(v12 -> {
                    Intent intentVideo = new Intent(DetailPostActivity.this, PreviewVideoActivity.class);
                    intentVideo.putExtra(LINK_VIDEO, ("" + BASE_URL + PART_POST + videoName));
                    intentVideo.putExtra(NAME_VIDEO, ("" + videoName));
                    startActivity(intentVideo);
                });
            }

        } else {
            linearLayoutVideos.setVisibility(View.INVISIBLE);
        }
        // END VIDEO


        // START FILES
        LinearLayout linearLayoutFiles = (LinearLayout) v.findViewById(R.id.idListFiles);

        if (postDTO.getDocs().size() > 0) {
            for (final String fileName : postDTO.getDocs()) {
                View fileItemView = getLayoutInflater().inflate(R.layout.item_detail_post_file, null);
                TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
                textView.setText(fileName);

                fileItemView.setOnClickListener(v1 -> {
                    if (isStoragePermissionGranted()) {
                        new FileLoadingTask(
                                BASE_URL + PART_POST + fileName,
                                new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName),
                                new FileLoadingListener() {
                                    @Override
                                    public void onBegin() {
                                        Toast.makeText(DetailPostActivity.this, "Begin download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DetailPostActivity.this, "Successful download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Throwable cause) {
                                        Toast.makeText(DetailPostActivity.this, "Error download", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onEnd() {

                                    }
                                }).execute();
                    }
                });
                linearLayoutFiles.addView(fileItemView, lParams);
            }
        } else {
            linearLayoutFiles.setVisibility(View.INVISIBLE);
        }

        // END FILES

        messageTextView.setText(ConvertorHTML.fromHTML(message));
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        /* Translate */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TranslateOptions options = TranslateOptions.newBuilder().setApiKey(Constants.CLOUD_API_KEY).build();
        Translate translate = options.getService();
        final Translation translation = translate.translate(messageTextView.getText().toString(), Translate.TranslateOption.targetLanguage(Locale.getDefault().getLanguage()));
        final Drawable drawable = getResources().getDrawable(R.drawable.verticalline);

        buttonTranslatePushMessage.setOnClickListener(v14 -> {
            messageTranslatePushMessage.setVisibility(View.VISIBLE);
            messageTranslatePushMessage.setText(translation.getTranslatedText());
            messageTranslatePushMessage.setBackground(drawable);
            buttonTranslatePushMessage.setVisibility(View.GONE);
        });

        messageTextView.setOnLongClickListener(v15 -> {
            String text = messageTextView.getText().toString();
            clipData = ClipData.newPlainText("text", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(DetailPostActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            return false;
        });

        linearLayout.addView(v);
        return linearLayout;
    }


    public void updateListView() {
        progressBar.showView();
        if (postDTO == null) {
            finish();
            return;
        }
        new Connection<ArrayList<CommentDTO>>().getListComments(postDTO.getId(), (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                arrayListComments = answerServerResponse.getDataTransferObject();
                adapterCommentsGroup = new AdapterCommentsGroup(DetailPostActivity.this, arrayListComments);
                listViewComments.setAdapter(adapterCommentsGroup);
                adapterCommentsGroup.notifyDataSetChanged();
            } else {
                Toast.makeText(DetailPostActivity.this, getString(R.string.errorServer), Toast.LENGTH_SHORT).show();
            }
            progressBar.dismissView();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_post_update, menu);

        if (userDTO.isAdmin() || postDTO.getUser_id().equals(new AppPreference(DetailPostActivity.this).getID())) {
            getMenuInflater().inflate(R.menu.menu_detail_post, menu);
        }

        if (userDTO.isAccessTranslate()) {
            getMenuInflater().inflate(R.menu.menu_detail_post_translation, menu);
        }
        return true;
    }

    public void changePost(Long post_id) {
        intentChangePost.putExtra(POST_ID, post_id);
        startActivityForResult(intentChangePost, 1);
    }

    public void translatePost(Long post_id) {
        Intent intentTranslatePost = new Intent(this, TranslatePostActivity.class);
        intentTranslatePost.putExtra(POST_ID, post_id);
        startActivityForResult(intentTranslatePost, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update:
                updateListView();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_edit:
                changePost(postDTO.getId());
                return true;
            case R.id.menu_delete:
                new Connection<String>().deletePost(postDTO, (isSuccess, answerServerResponse) -> {
                    if (isSuccess) {
                        Toast.makeText(DetailPostActivity.this, R.string.post_was_delete, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, NavigationActivity.class);
                        setResult(2, intent);
                        finish();
                    } else {
                        Toast.makeText(DetailPostActivity.this, R.string.errorServer, Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            case R.id.menu_translate_post:
                translatePost(postDTO.getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            linearLayout.removeAllViews();
            postDTO = PostDTO.findById(PostDTO.class, postDTO.getId());
            createHeaderPost();
            updateListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        state = listViewComments.onSaveInstanceState();
        super.onPause();
    }
}
