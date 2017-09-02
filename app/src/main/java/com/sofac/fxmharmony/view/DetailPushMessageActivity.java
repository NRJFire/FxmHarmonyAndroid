package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PushMessage;
import com.sofac.fxmharmony.util.ConvertorHTML;

import java.util.Locale;

import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class DetailPushMessageActivity extends BaseActivity {

    TextView titleDetailPushMessage;
    TextView dateDetailPushMessage;
    TextView messageDetailPushMessage;
    TextView buttonTranslatePushMessage;
    TextView messageTranslatePushMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_push_message);

        titleDetailPushMessage = (TextView) findViewById(R.id.titleDetailPushMessage);
        dateDetailPushMessage = (TextView) findViewById(R.id.dateDetailPushMessage);
        messageDetailPushMessage = (TextView) findViewById(R.id.messageDetailPushMessage);
        buttonTranslatePushMessage = (TextView) findViewById(R.id.idTranslatePush);
        messageTranslatePushMessage = (TextView) findViewById(R.id.messageTranslatePushMessage);

        setTitle(getString(R.string.push_message));



        Intent intent = getIntent();
        PushMessage pushMessage = (PushMessage) intent.getSerializableExtra(ONE_PUSH_MESSAGE_DATA);

        /* Translate */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TranslateOptions options = TranslateOptions.newBuilder().setApiKey(Constants.CLOUD_API_KEY).build();
        Translate translate = options.getService();
        final Translation translation = translate.translate(ConvertorHTML.fromHTML(pushMessage.getMessage()), Translate.TranslateOption.targetLanguage(Locale.getDefault().getLanguage()));
        final Drawable drawable = getResources().getDrawable(R.drawable.verticalline);

        if(pushMessage!=null){
            titleDetailPushMessage.setText(pushMessage.getTitle());
            dateDetailPushMessage.setText(pushMessage.getDate());
            messageDetailPushMessage.setText(ConvertorHTML.fromHTML(pushMessage.getMessage()));
        }

        buttonTranslatePushMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTranslatePushMessage.setText(translation.getTranslatedText());
                messageTranslatePushMessage.setBackground(drawable);
                buttonTranslatePushMessage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_push_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
