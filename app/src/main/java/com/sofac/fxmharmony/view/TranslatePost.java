package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.ConvertorHTML;

import java.util.ArrayList;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.POST_ID;


public class TranslatePost extends BaseActivity {

    public SharedPreferences preferences;
    public TextView postTextOrig;
    public EditText postTextEng;
    public EditText postTextKor;
    public EditText postTextRus;
    TabHost.TabSpec tabSpec;
    PostDTO postDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_post);
        setTitle(getString(R.string.translate_post));


        postDTO = PostDTO.findById(PostDTO.class, getIntent().getLongExtra(POST_ID, 0L));
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

        //TextView
        postTextOrig = (TextView) findViewById(R.id.id_text_orig);
        if (postDTO.getBody_original() != null && !"".equals(postDTO.getBody_original()))
            postTextOrig.setText(ConvertorHTML.fromHTML(postDTO.getBody_original()));

        //EditText
        postTextEng = (EditText) findViewById(R.id.id_text_eng);
        if (postDTO.getBody_en() != null && !"".equals(postDTO.getBody_en()))
            postTextEng.setText(ConvertorHTML.fromHTML(postDTO.getBody_en()));

        postTextKor = (EditText) findViewById(R.id.id_text_kor);
        if (postDTO.getBody_ko() != null && !"".equals(postDTO.getBody_ko()))
            postTextKor.setText(ConvertorHTML.fromHTML(postDTO.getBody_ko()));

        postTextRus = (EditText) findViewById(R.id.id_text_rus);
        if (postDTO.getBody_ru() != null && !"".equals(postDTO.getBody_ru()))
            postTextRus.setText(ConvertorHTML.fromHTML(postDTO.getBody_ru()));

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        tabHost.setup();

        tabSpec = tabHost.newTabSpec("eng");
        tabSpec.setIndicator(getString(R.string.text_eng));
        tabSpec.setContent(R.id.tab_eng);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("kor");
        tabSpec.setIndicator(getString(R.string.text_kor));
        tabSpec.setContent(R.id.tab_kor);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("rus");
        tabSpec.setIndicator(getString(R.string.text_rus));
        tabSpec.setContent(R.id.tab_rus);
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
        }

        // Вкладка по умолчанию активна
        tabHost.setCurrentTabByTag("eng");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:

                postDTO.setBody_ru(postTextRus.getText().toString());
                postDTO.setBody_en(postTextEng.getText().toString());
                postDTO.setBody_ko(postTextKor.getText().toString());

                new Connection<String>().updatePost(this, postDTO, new ArrayList<>(), new ArrayList<>(), (isSuccess, answerServerResponse) -> {
                    if (isSuccess) {
                        postDTO.save();
                        Intent intent = new Intent(TranslatePost.this, NavigationActivity.class);
                        setResult(2, intent);
                        finish();
                    } else {
                        if (answerServerResponse != null) {
                            Timber.e(answerServerResponse.toString());
                        } else {
                            Timber.e("SOME PROBLEM TO REQUEST ANSWER : null = answerServerResponse,       on up, check the log");
                        }
                        toastUpdateTranslateError();
                    }
                    progressBar.dismissView();
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toastUpdateTranslateError() {
        Toast.makeText(this, "Can't to save translate!", Toast.LENGTH_SHORT).show();
    }
}



