package com.herokuapp.shoppinglist.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.interfaces.GetUserCallback;
import com.herokuapp.shoppinglist.models.Credentials;
import com.herokuapp.shoppinglist.preferences.UserPreferences;
import com.herokuapp.shoppinglist.requests.UserRequests;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnLogin;
    TextView registerLink;
    EditText etId, etPassword;

    UserPreferences userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        userLocalStore = new UserPreferences(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String id = etId.getText().toString();
                String password = etPassword.getText().toString();
                Credentials user = new Credentials(id, password);
                authenticate(user);
                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void authenticate(Credentials user) {
        UserRequests userRequest = new UserRequests(this);

        userRequest.login(user, new GetUserCallback() {
            @Override
            public void done(String uid) {
                if (uid == null) {
                    showErrorMessage();
                } else {
                    logUserIn(uid);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage(R.string.login_dialog_message);
        dialogBuilder.setPositiveButton(R.string.login_dialog_button_positive, null);
        dialogBuilder.show();
    }

    private void logUserIn(String returnedUID) {
        userLocalStore.storeUserID(returnedUID);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
