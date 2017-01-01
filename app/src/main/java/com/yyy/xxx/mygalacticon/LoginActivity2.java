package com.yyy.xxx.mygalacticon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yyy.xxx.mygalacticon.Model.UserInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by len on 2016. 12. 30..
 */
public class LoginActivity2 extends AppCompatActivity{

    private static final String TAG = LoginActivity2.class.getName();
    private static final int REQUEST_SIGNUP = 200;
    private static final int REALMS = 100;
    private static final int SQLLITE = 101;

    private EditText input_email;
    private EditText input_password;
    private Button login;
    private TextView signupLink;
    private Realm mRealm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_realms);


        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        signupLink= (TextView) findViewById(R.id.link_signup);

        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
//        realm = Realm.getDefaultInstance();
        mRealm = Realm.getInstance(realmConfiguration);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Login");
                //login
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);

            }
        });

    }

    private void login() {

        Log.d(TAG, "login");

        if (!validate()){
            onLoginFail();
            return;
        }

        login.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity2.this, android.R.style.Theme_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        //TODO here is for Login logic.
        switch (UserInfo.getInstance().getKIND()){
            case REALMS :


                if (AuthorizationLogin()){
                    startActivity(new Intent(LoginActivity2.this, MainActivity.class));

            } else {

                    Toast.makeText(LoginActivity2.this, "Check your Password or ID", Toast.LENGTH_SHORT).show();
                }
                break;

            case SQLLITE :

                loginCheck();


                break;
        }



        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                } ,3000);
    }

    private void loginCheck() {

            String email = input_email.getText().toString();
            String password =  input_password.getText().toString();

            String result_password = LoginClassSQLlite.searchPassword(email);
            if (password.equals(result_password)){
               startActivity(new Intent(LoginActivity2.this, MainActivity.class));

            }else{
                Toast.makeText(getApplicationContext(),"PCheck your Password or ID", Toast.LENGTH_LONG).show();
            }
        }

    private boolean AuthorizationLogin() {

        RealmResults<UserInfo> query = mRealm.where(UserInfo.class)
                .contains("email",input_email.getText().toString())
                .contains("password",input_password.getText().toString())
                .findAll();

        Log.d(TAG, String.valueOf(query));

        if (query.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private void onLoginSuccess() {
        startActivity(new Intent(LoginActivity2.this, MainActivity.class));
        login.setEnabled(true);
        finish();
    }

    private void onLoginFail() {

        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        login.setEnabled(true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK){
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Toast.makeText(getApplicationContext(), "Success Registration",Toast.LENGTH_SHORT).show();

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {

            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }


}
