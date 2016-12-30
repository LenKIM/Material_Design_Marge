package com.yyy.xxx.mygalacticon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by len on 2016. 12. 30..
 */

public class LoginClassSQLlite extends SQLiteOpenHelper {


    public LoginClassSQLlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //새로운 테이블 생성, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼
        //item 문자열 생성, price 정수형 컬럼, create_at 문자열 정수로 구성된 테이블 생
//   ex)     db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
        db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String name, String email, String password){
        //읽고 쓰기가 가능한 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        //db에 입력한 행 값 추가하기
        db.execSQL("INSERT INTO USER VALUES(null,'" + name + "','"+email+"','"+password+"');");
        db.close();
    }

    public void update(String name, String password){
        SQLiteDatabase db = getWritableDatabase();
        //입력한 항목과 일치하는 행의 가격정보 수정
        db.execSQL("UPDATE USER SET price=" + password + " WHERE item='" + name + "';");
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM USER WHERE name='" + name + "';");
        db.close();
    }

//    public String getResult(){
//        //읽기가 가능하게 DB 열기
//        SQLiteDatabase db = getReadableDatabase();
//        String result = "";
//
//        //DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
//        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK",null);
//        while (cursor.moveToNext()) {
//            result += cursor.getString(0)
//                    + " : "
//                    + cursor.getString(1)
//                    + " | "
//                    + cursor.getInt(2)
//                    + cursor.getString(3)
//                    + "\n";
//        }
//        return  result;
//    }
}
