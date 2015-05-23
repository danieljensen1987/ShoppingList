package com.herokuapp.shoppinglist.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.interfaces.GetUserCallback;
import com.herokuapp.shoppinglist.models.Credentials;
import com.herokuapp.shoppinglist.preferences.Preferences;
import com.herokuapp.shoppinglist.requests.ListRequests;
import com.herokuapp.shoppinglist.requests.UserRequests;

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener{

    EditText etId, etPassword;
    Button btnRegister;
    Preferences userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String id = etId.getText().toString();
                String password = etPassword.getText().toString();
                Credentials user = new Credentials(id, password);
                registerUser(user);
//                finish();
                break;
        }
    }

    private void registerUser(Credentials user) {
        UserRequests serverRequest = new UserRequests(this);
        serverRequest.createUser(user, new GetUserCallback() {
            @Override
            public void done(String returnedUID) {
                userLocalStore = new Preferences(RegisterActivity.this);
                userLocalStore.storeUserID(returnedUID);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }
}
