package com.yyy.xxx.mygalacticon.Model;

import io.realm.Realm;

/**
 * Created by len on 2016. 12. 27..
 */
public interface AutoIncrementable {


    public void setPrimaryKey(int primaryKey);

    public int getNextPrimaryKey(Realm realm);

}
