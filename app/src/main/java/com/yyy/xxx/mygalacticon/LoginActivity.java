package com.yyy.xxx.mygalacticon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yyy.xxx.mygalacticon.Model.UserInfo;

/**
 * Created by len on 2016. 12. 30..
 */

public class LoginActivity extends AppCompatActivity {

    Button realm_login, sqllite_login;
    private static final int REALMS = 100;
    private static final int SQLLITE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);

        realm_login = (Button) findViewById(R.id.realms_login);
        sqllite_login = (Button) findViewById(R.id.shared_login);

        realm_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(LoginActivity.this, LoginActivity2.class);
                UserInfo.getInstance().setKIND(REALMS);
                startActivity(intent);
            }
        });


        sqllite_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginActivity2.class);
                UserInfo.getInstance().setKIND(SQLLITE);
                startActivity(intent);
            }
        });

    }
}
