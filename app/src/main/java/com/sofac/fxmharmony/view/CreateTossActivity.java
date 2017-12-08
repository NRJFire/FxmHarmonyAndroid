package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCreatePostMovies;
import com.sofac.fxmharmony.adapter.AdapterCreatePostPhotos;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.server.Connection;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_FILE;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_GALLERY_VIDEO;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_PHOTO;
import static com.sofac.fxmharmony.R.id.idButtonDeleting;

public class CreateTossActivity extends BaseActivity {

    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextBody)
    EditText editTextBody;

    @BindView(R.id.idLayoutPhotos)
    LinearLayout idLayoutPhotos;
    @BindView(R.id.idLayoutMovies)
    LinearLayout idLayoutMovies;
    @BindView(R.id.idLayoutFiles)
    LinearLayout idLayoutFiles;

    @BindView(R.id.idMenuButton)
    FloatingActionMenu idMenuButton;

    @BindView(R.id.idListPhotos)
    RecyclerView idListPhotos;
    @BindView(R.id.idListMovies)
    RecyclerView idListMovies;

    public ArrayList<Uri> listPhoto = new ArrayList<>();
    public ArrayList<Uri> listMovies = new ArrayList<>();
    public ArrayList<Uri> listFiles = new ArrayList<>();
    public HashMap<String, Integer> managers = new HashMap<>();
    AdapterCreatePostPhotos adapterCreatePostPhotos;
    AdapterCreatePostMovies adapterCreatePostMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_toss);
        ButterKnife.bind(this);
        setTitle("Create new toss");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        idMenuButton.setClosedOnTouchOutside(true);

        adapterCreatePostPhotos = new AdapterCreatePostPhotos(listPhoto);
        adapterCreatePostPhotos.setItemClickListener((view, position) -> {
            switch (view.getId()) {
                case idButtonDeleting:
                    listPhoto.remove(position);
                    adapterCreatePostPhotos.notifyDataSetChanged();
                    if (listPhoto.isEmpty()) idLayoutPhotos.setVisibility(View.GONE);
                    break;
            }
        });

        idListPhotos.setAdapter(adapterCreatePostPhotos);
        idListPhotos.setHasFixedSize(true);
        idListPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterCreatePostMovies = new AdapterCreatePostMovies(listMovies);
        adapterCreatePostMovies.setItemClickListener((view, position) -> {
            switch (view.getId()) {
                case idButtonDeleting:
                    listMovies.remove(position);
                    adapterCreatePostMovies.notifyDataSetChanged();
                    if (listMovies.isEmpty()) idLayoutMovies.setVisibility(View.GONE);
                    break;
            }
        });
        idListMovies.setAdapter(adapterCreatePostMovies);
        idListMovies.setHasFixedSize(true);
        idListMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.send_post_button:
                if (!editTextBody.getText().toString().equals("") && !editTextTitle.getText().toString().isEmpty()) {
                    requestCreateToss();
                } else {
                    showToast("Please input text in all fields");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestCreateToss() {
        progressBar.showView();
        ArrayList<Uri> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(listPhoto);
        arrayListAll.addAll(listMovies);
        arrayListAll.addAll(listFiles);

        managers.put("0",11);

        new Connection<String>().addToss(this, new SenderContainerDTO(appPreference.getID(), editTextTitle.getText().toString(), "", managers , editTextBody.getText().toString()), arrayListAll, (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                Intent intent = new Intent(this, NavigationActivity.class);
                setResult(2, intent);
                finish();
            } else {
                showToast("Some problem with creating post!");
            }
            progressBar.dismissView();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            final Uri fileUri = data.getData();

            if (requestCode == REQUEST_TAKE_PHOTO) {

                for (Uri urlPhoto : listPhoto) {
                    if (fileUri.equals(urlPhoto)) return;
                }
                listPhoto.add(fileUri);
                adapterCreatePostPhotos.notifyDataSetChanged();
                idLayoutPhotos.setVisibility(View.VISIBLE);

            } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                for (Uri urlMovie : listMovies) {
                    if (fileUri.equals(urlMovie)) return;
                }
                listMovies.add(fileUri);
                adapterCreatePostMovies.notifyDataSetChanged();
                idLayoutMovies.setVisibility(View.VISIBLE);

            } else if (requestCode == REQUEST_TAKE_FILE) {
                for (Uri urlFiles : listFiles) {
                    if (fileUri.equals(urlFiles)) return;
                }
                listFiles.add(fileUri);
                idLayoutFiles.addView(createViewFile(fileUri));
                idLayoutFiles.setVisibility(View.VISIBLE);

            }

        }

    }

    public View createViewFile(final Uri fileUri) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);

        ((TextView) view.findViewById(R.id.idTextFile)).setText(FilenameUtils.getName(fileUri.toString()));

        (view.findViewById(R.id.idButtonDeleting)).setOnClickListener(v -> {
            idLayoutFiles.removeView(view);
            listFiles.remove(fileUri);
            if (listFiles.isEmpty()) idLayoutFiles.setVisibility(View.GONE);
        });
        return view;
    }


    @OnClick({R.id.buttonAddFiles, R.id.buttonAddMovies, R.id.buttonAddPhotos})
    public void onViewClicked(View view) {
        if (isStoragePermissionGranted()) {
            switch (view.getId()) {
                case R.id.buttonAddFiles:
                    choiceFiles();
                    break;
                case R.id.buttonAddMovies:
                    choiceMovies();
                    break;
                case R.id.buttonAddPhotos:
                    choicePhotos();
                    break;
            }
            idMenuButton.toggle(true);
        }
    }

    public void choicePhotos() {
        Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        takePhotoIntent.setType("image/*");
        startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
    }

    public void choiceMovies() {
        Intent takeVideoIntent = new Intent();
        takeVideoIntent.setType("video/*");
        takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void choiceFiles() {
        Intent takeFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        takeFileIntent.setType("*/*");
        startActivityForResult(takeFileIntent, REQUEST_TAKE_FILE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "*/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, REQUEST_TAKE_FILE);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }


}




