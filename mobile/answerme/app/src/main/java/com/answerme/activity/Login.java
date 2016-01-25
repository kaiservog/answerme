package com.answerme.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.answerme.dao.*;
import com.answerme.model.User;

import org.androidannotations.annotations.EActivity;
import mobile.answerme.com.answerme.R;

/**
 * Created by rctoscano on 23/01/2016.
 */
@EActivity(R.layout.activity_login)
public class Login extends Activity implements View.OnClickListener, Runnable {

    CreateTables createTables;

    private User user;

    private UserDAO userDAO;

    private ProgressDialog dialog;

    /**
     * Variaveis activity
     */
    EditText txtUsername;
    EditText txtPassword;


    String username;
    String password;


    public void onClick(View view){
        dialog = ProgressDialog.show(this, "Login", "Autenticando usuário, por favor aguarde...", false, true);
        new Thread(this).start();
    }

    @Override
    public void run() {

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        username = txtUsername.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        try{
            user = new User();
            userDAO = new UserDAO(this);

            user.setEmail(username);
            user.setPassword(password);


            createTables = new CreateTables();
            createTables.create(this);


        }
        catch(Exception e){
            Log.e("Login", "Error in Login", e);
        }

    }
}
