package com.sofac.fxmharmony.view;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.dto.CommentDTO;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.ConvertorHTML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.DELETE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_COMMENTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_COMMENT_REQUEST;

public class DetailPostActivity extends BaseActivity {

    Button buttonSend;
    Intent intent;
    View headerView;
    ArrayList<CommentDTO> arrayListComments;
    ListView listViewComments;
    PostDTO postDTO;
    EditText editTextComment;
    AdapterCommentsGroup adapterCommentsGroup;
    SharedPreferences preferences;
    public static Long idComment = 0L;
    public static Boolean isCreatingComment = true;
    public static CommentDTO commentDTO;
    public Intent intentChangePost;
    ClipboardManager clipboardManager;
    ClipData clipData;
    LinearLayout linearLayout;
    Parcelable state;

    TextView buttonTranslatePushMessage;
    TextView messageTranslatePushMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        setTitle(getString(R.string.FXM_group));

        if (state != null) {
            listViewComments.onRestoreInstanceState(state);
        }

        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        linearLayout = new LinearLayout(this);


        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        intentChangePost = new Intent(this, ChangePost.class);
        intent = getIntent();

        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);

        buttonSend = (Button) findViewById(R.id.sendComment);
        editTextComment = (EditText) findViewById(R.id.edit_text_comment);
        listViewComments = (ListView) findViewById(R.id.idListViewComments);

        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (arrayListComments != null) {
                    if (position > 0) {
                        editTextComment.append(arrayListComments.get(position - 1).getUserName() + ", ");
                    }
                }
            }
        });

        listViewComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    commentDTO = arrayListComments.get(position - 1);
                    if (commentDTO.getServerID() != null) {
                        DetailPostActivity.idComment = commentDTO.getServerID();
                        if (commentDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailPostActivity.this);
                            builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which) {
                                        case 0: //Edit
                                            editTextComment.setText("");
                                            editTextComment.append(commentDTO.getCommentText());
                                            isCreatingComment = false;
                                            break;
                                        case 1: //Delete
                                            new GroupExchangeOnServer<>(DetailPostActivity.idComment, true, DELETE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                                                @Override
                                                public void processFinish(Boolean isSuccess, String answer) {
                                                    if (isSuccess) {
                                                        updateListView();
                                                        Toast.makeText(DetailPostActivity.this, R.string.comment_was_delete, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).execute();
                                            break;
                                    }
                                }
                            });
                            builder.show();
                        }
                    } else {
                        Toast.makeText(DetailPostActivity.this, R.string.problem_with_ID_comment, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editTextComment.getText().toString()).isEmpty()) {
                    if (isCreatingComment) { //Создание коментария
                        new GroupExchangeOnServer<>(new CommentDTO(1L, null, (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getId(), null), true, WRITE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                            @Override
                            public void processFinish(Boolean isSuccess, String answer) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                } else {
                                    Toast.makeText(DetailPostActivity.this, R.string.create_comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    } else { // Редактирование коментария
                        new GroupExchangeOnServer<>(new CommentDTO(1L, commentDTO.getServerID(), (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getId(), null), true, UPDATE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                            @Override
                            public void processFinish(Boolean isSuccess, String answer) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                    isCreatingComment = true;
                                } else {
                                    Toast.makeText(DetailPostActivity.this, R.string.edit_comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    }

                } else {
                    Toast.makeText(DetailPostActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initialHeaderPost();
        updateListView();
    }

    public void createHeaderPost() {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringsSpinnerLanguage);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguage.setAdapter(adapter);
        if (stringsSpinnerLanguage.isEmpty()) spinnerLanguage.setVisibility(View.INVISIBLE);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem().toString() == getString(R.string.original_spinner)) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_original()));

                } else if (parent.getSelectedItem().toString() == getString(R.string.english_spinner)) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_en()));

                } else if (parent.getSelectedItem().toString() == getString(R.string.korean_spinner)) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_ko()));

                } else if (parent.getSelectedItem().toString() == getString(R.string.russian_spinner)) {
                    ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(ConvertorHTML.fromHTML(postDTO.getBody_ru()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initialHeaderPost() {
        if (postDTO != null) {
            createHeaderPost();
            listViewComments.addHeaderView(headerView, null, false);
        }
    }

    TextView messageTextView;

    View createPostView(String name, String date, String message) {
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = getLayoutInflater().inflate(R.layout.post_view_detail, null);

        // START AVATAR
        Uri uri = Uri.parse(BASE_URL + Constants.PART_URL_FILE_AVATAR + postDTO.getAvatar());
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

        // START IMAGE
        LinearLayout linearLayoutPhotos = (LinearLayout) v.findViewById(R.id.idListPhotos);

//        if (null != postDTO.getLinksImage() && !"".equals(postDTO.getLinksImage()) && postDTO.getLinksImage().length() > 5) {
//            for (final String imageName : postDTO.getLinksImage().split(";#")) {
//
//                View photoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_photo, null);
//                ImageView imageView = (ImageView) photoItemView.findViewById(R.id.idItemPhoto);
//
//                Uri uriImage = Uri.parse(BASE_URL + PART_URL_FILE_IMAGE_POST + imageName);
//                Glide.with(this)
//                        .load(uriImage)
//                        .error(R.drawable.no_image)
//                        .placeholder(R.drawable.no_image)
//                        .into(imageView);
//                linearLayoutPhotos.addView(photoItemView, lParams);
//                photoItemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intentPhoto = new Intent(DetailPostActivity.this, PreviewPhotoActivity.class);
//                        intentPhoto.putExtra(LINK_IMAGE, ("" + BASE_URL + PART_URL_FILE_IMAGE_POST + imageName));
//                        intentPhoto.putExtra(NAME_IMAGE, ("" + imageName));
//                        startActivity(intentPhoto);
//                    }
//                });
//            }
//
//        } else {
//            linearLayoutPhotos.setVisibility(View.INVISIBLE);
//        }
        // END IMAGE


//        // START VIDEO
//        LinearLayout linearLayoutVideos = (LinearLayout) v.findViewById(R.id.idListVideos);
//
//        if (null != postDTO.getLinksVideo() && !"".equals(postDTO.getLinksVideo()) && postDTO.getLinksVideo().length() > 5) {
//            for (final String videoName : postDTO.getLinksVideo().split(";#")) {
//
//                View videoItemView = getLayoutInflater().inflate(R.layout.item_detail_post_video, null);
//                ImageView imageVideoView = (ImageView) videoItemView.findViewById(R.id.idItemVideo);
//
//                Uri uriVideo = Uri.parse(BASE_URL + GET_POST_thumbnails_END_URL + videoName + ".png");
//                Glide.with(this)
//                        .load(uriVideo)
//                        .error(R.drawable.no_video)
//                        .placeholder(R.drawable.no_video)
//                        .into(imageVideoView);
//                linearLayoutVideos.addView(videoItemView, lParams);
//
//                videoItemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intentVideo = new Intent(DetailPostActivity.this, PreviewVideoActivity.class);
//                        intentVideo.putExtra(LINK_VIDEO, ("" + BASE_URL + GET_POST_FILES_END_URL + videoName));
//                        intentVideo.putExtra(NAME_VIDEO, ("" + videoName));
//                        startActivity(intentVideo);
//                    }
//                });
//            }
//
//        } else {
//            linearLayoutVideos.setVisibility(View.INVISIBLE);
//        }
//        // END VIDEO


//        // START FILES
//        LinearLayout linearLayoutFiles = (LinearLayout) v.findViewById(R.id.idListFiles);
//
//        if (null != postDTO.getLinksFile() && !"".equals(postDTO.getLinksFile()) && postDTO.getLinksFile().length() > 5) {
//            for (final String fileName : postDTO.getLinksFile().split(";#")) {
//                View fileItemView = getLayoutInflater().inflate(R.layout.item_detail_post_file, null);
//                TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
//                textView.setText(fileName);
//
//                Log.e("FILES URL", BASE_URL + GET_POST_FILES_END_URL + fileName);
//                fileItemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new FileLoadingTask(
//                                BASE_URL + GET_POST_FILES_END_URL + fileName,
//                                new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName),
//                                new FileLoadingListener() {
//                                    @Override
//                                    public void onBegin() {
//                                        Toast.makeText(DetailPostActivity.this, "Begin download", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onSuccess() {
//                                        Toast.makeText(DetailPostActivity.this, "Successful download", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Throwable cause) {
//                                        Toast.makeText(DetailPostActivity.this, "Error download", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onEnd() {
//
//                                    }
//                                }).execute();
//                    }
//                });
//                linearLayoutFiles.addView(fileItemView, lParams);
//            }
//        } else {
//            linearLayoutFiles.setVisibility(View.INVISIBLE);
//        }
//        // END FILES


        messageTextView.setText(ConvertorHTML.fromHTML(message));
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                /* Translate */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TranslateOptions options = TranslateOptions.newBuilder().setApiKey(Constants.CLOUD_API_KEY).build();
        Translate translate = options.getService();
        final Translation translation = translate.translate(messageTextView.getText().toString(), Translate.TranslateOption.targetLanguage(Locale.getDefault().getLanguage()));
        final Drawable drawable = getResources().getDrawable(R.drawable.verticalline);

        buttonTranslatePushMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTranslatePushMessage.setVisibility(View.VISIBLE);
                messageTranslatePushMessage.setText(translation.getTranslatedText());
                messageTranslatePushMessage.setBackground(drawable);
                buttonTranslatePushMessage.setVisibility(View.GONE);
            }
        });

        messageTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text = messageTextView.getText().toString();
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(DetailPostActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        linearLayout.addView(v);
        return linearLayout;
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    public void updateListView() {
        new GroupExchangeOnServer<>(postDTO.getId(), true, LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
            @Override
            public void processFinish(Boolean output, String answer) {
                arrayListComments = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);
                adapterCommentsGroup = new AdapterCommentsGroup(DetailPostActivity.this, arrayListComments);

                listViewComments.setAdapter(adapterCommentsGroup);
                adapterCommentsGroup.notifyDataSetChanged();
            }
        }).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_post_update, menu);
        UserDTO userDTO = UserDTO.findById(UserDTO.class, new AppUserID(DetailPostActivity.this).getID());
        //PermissionDTO permissionDTO = PermissionDTO.findById(PermissionDTO.class, getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L));

        //Timber.e("!!!!!!! permissionDTO !!!!!!! "+permissionDTO);

        if (userDTO.isAdmin() || postDTO.getUser_id().equals(new AppUserID(DetailPostActivity.this).getID())) {
            getMenuInflater().inflate(R.menu.menu_detail_post, menu);
        }

//        if (permissionDTO.getTranslatePermission() != null && permissionDTO.getTranslatePermission()) {
//            getMenuInflater().inflate(R.menu.menu_detail_post_translation, menu);
//        }
        return true;
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
                intentChangePost.putExtra(ONE_POST_DATA, postDTO);
                startActivityForResult(intentChangePost, 1);
                return true;
            case R.id.menu_delete:
                new GroupExchangeOnServer<>(postDTO.getId(), true, DELETE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                    @Override
                    public void processFinish(Boolean isSuccess, String answer) {
                        if (isSuccess) {
                            Toast.makeText(DetailPostActivity.this, R.string.post_was_delete, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).execute();
                return true;
            case R.id.menu_translate_post:
                Intent intentTranslatePost = new Intent(this, TranslatePost.class);
                intentTranslatePost.putExtra(ONE_POST_DATA, postDTO);
                startActivityForResult(intentTranslatePost, 1);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            Toast.makeText(this, "CHANGE!!!", Toast.LENGTH_SHORT).show();
            linearLayout.removeAllViews();
            postDTO = (PostDTO) data.getSerializableExtra(ONE_POST_DATA);
            createHeaderPost();
            updateListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        state = listViewComments.onSaveInstanceState();
        super.onPause();
    }
}
