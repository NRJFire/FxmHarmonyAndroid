package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCreatePostMovies;
import com.sofac.fxmharmony.adapter.AdapterCreatePostPhotos;
import com.sofac.fxmharmony.dto.ManagerDTO;
import com.sofac.fxmharmony.dto.ResponsibleUserDTO;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.server.Connection;

import org.apache.commons.io.FilenameUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

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
    @BindView(R.id.textViewChoiceDate)
    TextView textViewChoiceDate;

    @BindView(R.id.choosedUsers)
    TextView choosedUsers;

    public ArrayList<Uri> listPhoto = new ArrayList<>();
    public ArrayList<Uri> listMovies = new ArrayList<>();
    public ArrayList<Uri> listFiles = new ArrayList<>();
    ArrayList<ResponsibleUserDTO> responsibleUserDTOS = new ArrayList<>();
    AdapterCreatePostPhotos adapterCreatePostPhotos;
    AdapterCreatePostMovies adapterCreatePostMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_toss);
        ButterKnife.bind(this);
        setTitle("Creating new toss");

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

        new Connection<ArrayList<ResponsibleUserDTO>>().getMannagers(appPreference.getID(), (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                responsibleUserDTOS.clear();
                responsibleUserDTOS.addAll(answerServerResponse.getDataTransferObject());
            } else {
                showToast("Error getting manager!");
            }
        });

        choosedUsers.setText("No responsible users");


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
                if (!editTextBody.getText().toString().equals("") && !editTextTitle.getText().toString().isEmpty() /*&& !"".equals(statusToss)*/) {
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

        ArrayList<Uri> arrayListAll = new ArrayList<>();
        arrayListAll.addAll(listPhoto);
        arrayListAll.addAll(listMovies);
        arrayListAll.addAll(listFiles);

        HashMap<String, Integer> managers = new HashMap<>();
        for (int i = 0; i < selectedManagers.size(); i++) {
            Integer selectedListID = selectedManagers.get(i);
            ResponsibleUserDTO responsibleUserDTO = responsibleUserDTOS.get(selectedListID);
            Integer id = Integer.parseInt(responsibleUserDTO.getId());
            managers.put(String.valueOf(i), id);
        }

        progressBar.showView();
        new Connection<String>().addToss(
                this,
                new SenderContainerDTO(
                        appPreference.getID(),
                        editTextTitle.getText().toString(),
                        textViewChoiceDate.toString(),
                        managers,
                        editTextBody.getText().toString()),
                arrayListAll,
                (isSuccess, answerServerResponse) -> {
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


    @OnClick({R.id.buttonAddFiles, R.id.buttonAddMovies, R.id.buttonAddPhotos, R.id.buttonChoiceDate, R.id.buttonChoiceManagers})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.buttonAddFiles:
                if (isStoragePermissionGranted()) {
                    choiceFiles();
                }
                idMenuButton.toggle(true);
                break;
            case R.id.buttonChoiceManagers:
                choiceManagers();
                break;
            case R.id.buttonAddMovies:
                if (isStoragePermissionGranted()) {
                    choiceMovies();
                }
                idMenuButton.toggle(true);
                break;
            case R.id.buttonAddPhotos:
                if (isStoragePermissionGranted()) {
                    choicePhotos();
                }
                idMenuButton.toggle(true);
                break;
            case R.id.buttonChoiceDate:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
                        .build()
                        .show();
                break;
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

    AlertDialog filterDialog;

    ArrayList<Integer> selectedManagers = new ArrayList<>();

    public void choiceManagers() {

        String[] namesManagers = new String[responsibleUserDTOS.size()];
        for (int i = 0; i < responsibleUserDTOS.size(); i++) {
            namesManagers[i] = responsibleUserDTOS.get(i).getName();
        }

        boolean[] checkedItems = new boolean[namesManagers.length];

        for (int i = 0; i < namesManagers.length; i++) {
            if (selectedManagers.contains(i)) {
                checkedItems[i] = true;
            } else {
                checkedItems[i] = false;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(namesManagers, checkedItems, (dialog, indexSelected, isChecked) -> {
            if (isChecked) {
                selectedManagers.add(indexSelected);
            } else if (selectedManagers.contains(indexSelected)) {
                selectedManagers.remove(Integer.valueOf(indexSelected));
            }
        }).setPositiveButton("OK", (dialog, id) -> {
            if (!selectedManagers.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < selectedManagers.size(); i++) {
                    stringBuilder.append(String.format("%s, ", responsibleUserDTOS.get(selectedManagers.get(i)).getName()));
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                choosedUsers.setText(stringBuilder.toString());
            } else {
                choosedUsers.setText("No responsible users");
            }
        }).setNegativeButton("Cancel", (dialog, id) -> filterDialog.dismiss());

        filterDialog = builder.create();
        filterDialog.show(); // only works when I show the dialog first, but I want every option to be selected without showing first

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

    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            textViewChoiceDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(date));
        }

        @Override
        public void onDateTimeCancel() {

        }
    };

}




