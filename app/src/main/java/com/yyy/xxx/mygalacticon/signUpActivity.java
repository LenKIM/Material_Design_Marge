package com.yyy.xxx.mygalacticon;

import android.app.ProgressDialog;
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

/**
 * Created by len on 2016. 12. 30..
 */

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();
    private static final int REALMS = 100;
    private static final int SQLLITE = 101;
    private static final int NOT_FOUND = 0;

    private Realm realm;
    private EditText input_signUpName;
    private EditText input_signUpEmail;
    private EditText input_signUpPassowrd;
    private Button sinUpButton;
    private TextView link_login;

    LoginClassSQLlite SQLliteSignUp = new LoginClassSQLlite(SignUpActivity.this,"USER.db",null,1);




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
//        realm = Realm.getDefaultInstance();
        realm = Realm.getInstance(realmConfiguration);

        input_signUpEmail = (EditText) findViewById(R.id.input_email);
        input_signUpPassowrd = (EditText) findViewById(R.id.input_password);
        input_signUpName = (EditText) findViewById(R.id.input_name);
        sinUpButton = (Button) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.return_login);


        sinUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void signup() {
        Log.d(TAG, "signup");

        if (!validate()){
            onSignupFailed();
            return;
        }

        sinUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                android.R.style.Theme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = input_signUpName.getText().toString();
        final String email = input_signUpEmail.getText().toString();
        final String password = input_signUpPassowrd.getText().toString();

        //받은 인텐트의 값에 따라서 다르게 동작하게 만들기.

        int kind = UserInfo.getInstance().getKIND();

        if (kind == 0){
            Toast.makeText(SignUpActivity.this,"NOT FOUND", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (kind){
            case REALMS :
                //By using REALMS, make to UserDATA
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        UserInfo userInfo = realm.createObject(UserInfo.class, 0);
                        userInfo.set_id(userInfo.getNextPrimaryKey(realm));
                        userInfo.setName(name);
                        userInfo.setEmail(email);
                        userInfo.setPassword(password);
                    }
                });
                break;

            case SQLLITE :
                //By using REALMS, make to UserSQLliteDATA
                SQLliteSignUp.insert(name,email,password);

                //TODO 가능하면 비밀번호 변경도 넣기.
                break;

        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        }

    private void onSignupSuccess() {
        sinUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }


    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
        sinUpButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = input_signUpName.getText().toString();
        String email = input_signUpEmail.getText().toString();
        String password = input_signUpPassowrd.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            input_signUpName.setError("at least 3 characters");
            valid = false;
        } else {
            input_signUpName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_signUpEmail.setError("enter a valid email address");
            valid = false;
        } else {
            input_signUpEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_signUpPassowrd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_signUpPassowrd.setError(null);
        }

        return valid;

    }


}


