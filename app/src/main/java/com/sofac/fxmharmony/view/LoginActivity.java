package com.sofac.fxmharmony.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.AuthorizationDTO;
import com.sofac.fxmharmony.dto.CustomerDTO;
import com.sofac.fxmharmony.dto.ManagerDTO;
import com.sofac.fxmharmony.dto.StaffDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.CheckAuthorization;

/**
 * Activity login & password authorization, validation input field, if validate data start MainActivity.class
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Intent intent;
    EditText editPassword, editLogin;
    Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        buttonLogin.setOnClickListener(this);
        intent = new Intent(this, NavigationActivity.class);

        // Cleaning DataBase
        try {
            ManagerDTO.deleteAll(UserDTO.class);
            CustomerDTO.deleteAll(UserDTO.class);
            StaffDTO.deleteAll(UserDTO.class);
            UserDTO.deleteAll(UserDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        editPassword = (EditText) findViewById(R.id.editPassword);
        editLogin = (EditText) findViewById(R.id.editLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    @Override
    public void onClick(View v) {
        progressBar.showView();

        String password = editPassword.getText().toString();
        String login = editLogin.getText().toString();
        if ("".equals(password) && "".equals(login)) {
            Toast.makeText(LoginActivity.this, getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            new Server<UserDTO>().authorizationUser(new AuthorizationDTO(editLogin.getText().toString(), editPassword.getText().toString(), sharedPref.getString(Constants.GOOGLE_CLOUD_PREFERENCE, "")), new Server.AnswerServerResponse<UserDTO>() {
                @Override
                public void processFinish(Boolean isSuccess, ServerResponse<UserDTO> answerServerResponse) {
                    if (isSuccess) {

                        UserDTO userDTO = answerServerResponse.getDataTransferObject();
                        userDTO.save();

                        new CheckAuthorization(LoginActivity.this).setAuthorization(true);
                        new AppUserID(LoginActivity.this).setID(userDTO.getId());

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        progressBar.dismissView();
                        startActivity(intent);

                    } else {
                        progressBar.dismissView();
                        Toast.makeText(LoginActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    /**
     * Save login & pass
     */

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Username", editLogin.getText().toString());
        editor.putString("Password", editPassword.getText().toString());
        editor.apply();
        editor.commit();
    }

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editLogin.setText(settings.getString("Username", ""));
        editPassword.setText(settings.getString("Password", ""));
    }

    /**
     * Back press
     */
    private static long backPressed;

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

}
