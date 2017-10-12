package com.sofac.fxmharmony.view.fragmentDialog;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

import com.sofac.fxmharmony.R;

import java.util.Locale;

public class ChangeLanguageFragmentDialog extends DialogFragment {

    private RadioGroup radioGroup;


    private Button changeLanguageButton;
    private String currentLanguage = "";

    public static ChangeLanguageFragmentDialog newInstance() {
        return new ChangeLanguageFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View languageFragmentDialog = inflater.inflate(R.layout.dialog_change_language, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        radioGroup = (RadioGroup) languageFragmentDialog.findViewById(R.id.radioGroupLanguage);
        changeLanguageButton = (Button) languageFragmentDialog.findViewById(R.id.changeLanguageButton);

        currentLanguage = Locale.getDefault().getLanguage();

        if (currentLanguage.equals("uk")) radioGroup.check(R.id.radioUkrainian);
        if (currentLanguage.equals("ru")) radioGroup.check(R.id.radioRussian);
        if (currentLanguage.equals("ko")) radioGroup.check(R.id.radioKorean);
        if (currentLanguage.equals("en")) radioGroup.check(R.id.radioEnglish);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioUkrainian:
                        currentLanguage = "uk";
                        break;
                    case R.id.radioRussian:
                        currentLanguage = "ru";
                        break;
                    case R.id.radioKorean:
                        currentLanguage = "ko";
                        break;
                    case R.id.radioEnglish:
                        currentLanguage = "en";
                        break;
                    default:
                        break;
                }
            }
        });



        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(getActivity(), currentLanguage);
            }
        });

        return languageFragmentDialog;
    }


    private void setLocale(Context context, String mLang) {


        SharedPreferences sPref = getActivity().getSharedPreferences("Locale", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("Locale", mLang);
        ed.apply();


        Locale mNewLocale = new Locale(mLang);
        Locale.setDefault(mNewLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = mNewLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        getActivity().finish();
    }

}

