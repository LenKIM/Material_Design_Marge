package com.yyy.xxx.mygalacticon.Model;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by len on 2016. 12. 30..
 */

public class UserInfo extends RealmObject implements Serializable,AutoIncrementable{

    private static final long serialVersionUID = -2961357772171639609L;

    private static UserInfo instance = new UserInfo();

    private String name;
    private String email;
    private String password;
    private int KIND;

    private Integer _id;

    public static synchronized UserInfo getInstance(){
        return instance;
    }

//    private UserInfo(String name, String email, String password){
//        name = null;
//        email = null;
//        password = null;
//    }

    public UserInfo(){
        name = null;
        email = null;
        password = null;
        KIND = 0;

    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public int getKIND() {
        return KIND;
    }

    public void setKIND(int KIND) {
        this.KIND = KIND;
    }


    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public void setPrimaryKey(int primaryKey) {
        this._id = primaryKey;
    }

    @Override
    public int getNextPrimaryKey(Realm realm) {
        return realm.where(UserInfo.class).max("_id").intValue() + 1;
    }
}
