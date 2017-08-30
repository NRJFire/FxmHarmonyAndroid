package com.sofac.fxmharmony.view.fragmentDialog;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.SettingsExchangeOnServer;
import com.sofac.fxmharmony.data.dto.ChangeNameDTO;
import com.sofac.fxmharmony.util.AppMethods;


import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.CHANGE_NAME_REQUEST;
import static com.sofac.fxmharmony.Constants.DELETE_AVATAR_REQUEST;
import static com.sofac.fxmharmony.R.id.textView;

public class ChangeNameFragmentDialog extends DialogFragment {

    private EditText newUserNameInput;
    private Button changeNameButton;

    public static ChangeNameFragmentDialog newInstance() {
        return new ChangeNameFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changeNameFragmentDialog = inflater.inflate(R.layout.dialog_change_name, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        newUserNameInput = (EditText) changeNameFragmentDialog.findViewById(R.id.newUserNameInput);
        changeNameButton = (Button) changeNameFragmentDialog.findViewById(R.id.changeNameButton);


        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          /*      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(Constants.CLOUD_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(newUserNameInput.getText().toString(),
                                Translate.TranslateOption.targetLanguage("de"));
                Toast.makeText(getActivity(), translation.getTranslatedText(), Toast.LENGTH_SHORT).show();*/

                final String newName = newUserNameInput.getText().toString();

                if (newName.length() == 0) {
                    newUserNameInput.setError(getActivity().getString(R.string.empty_name_error));
                    return;
                }

                if (newName.length() < 3 || newName.length() > 16) {
                    newUserNameInput.setError(getActivity().getString(R.string.size_name_error));
                    return;
                }


                ChangeNameDTO changeNameDTO = new ChangeNameDTO(AppMethods.getUserId(getActivity()), newName);

                new SettingsExchangeOnServer<ChangeNameDTO>(changeNameDTO, CHANGE_NAME_REQUEST, getActivity(), new SettingsExchangeOnServer.SettingsAsyncResponse() {
                    @Override
                    public void processFinish(Boolean isSuccess) {
                        if (isSuccess) {
                            AppMethods.saveUserName(getActivity(), newName);
                            getDialog().cancel();
                        }
                    }
                }).execute();




            }
        });


        return changeNameFragmentDialog;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

}
