package com.example.quarantaine.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import dk.quarantaine.commons.dto.OauthTokenResponseDTO;

public class AccessTokenDatabaseAccess extends SQLiteOpenHelper {


    private static final String ACCESSTOKEN_TABLE = "ACCESSTOKEN";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String VALIDITY = "validity";
    private static final String TOKEN_TYPE = "token_type";
    private static final String USERNAME = "username";



    public AccessTokenDatabaseAccess(@Nullable Context context) {
        super(context, "quarantaine.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ACCESSTOKEN_TABLE + " (" + USERNAME + " TEXT PRIMARY KEY NOT NULL, " + ACCESS_TOKEN + " TEXT, " + REFRESH_TOKEN + " TEXT, "+ TOKEN_TYPE +" TEXT, "+ VALIDITY + " DATE)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertOrUpdateAccessToken(String username, OauthTokenResponseDTO tokenResponseDTO){
        if(doesEntryExist(username)){
            return updateData(username,tokenResponseDTO);
        }
        else {
            return insertData(username,tokenResponseDTO);
        }
    }

    public boolean insertData (String username, OauthTokenResponseDTO tokenResponseDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCESS_TOKEN, tokenResponseDTO.getAccess_token());
        values.put(REFRESH_TOKEN, tokenResponseDTO.getRefresh_token());
        values.put(VALIDITY, (LocalDateTime.now().plusSeconds(tokenResponseDTO.getValidity())).toString());
        values.put(TOKEN_TYPE, tokenResponseDTO.getToken_type());
        values.put(USERNAME, username);

        long insert = db.insert(ACCESSTOKEN_TABLE, null, values);

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String username, OauthTokenResponseDTO tokenResponseDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCESS_TOKEN, tokenResponseDTO.getAccess_token());
        values.put(REFRESH_TOKEN, tokenResponseDTO.getRefresh_token());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,tokenResponseDTO.getValidity());
        Date time = cal.getTime();



        values.put(VALIDITY, time.toString());
        values.put(TOKEN_TYPE, tokenResponseDTO.getToken_type());


        String[] usernamevalue = new String[]{username};

        long insert = db.update(ACCESSTOKEN_TABLE, values,  USERNAME + " = ?", usernamevalue );

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    private boolean doesEntryExist(String username){
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tables = new String[]{USERNAME};
        String[] selectionArg = new String[]{username};
        Cursor cursor = db.query(
                ACCESSTOKEN_TABLE,
                tables,
                USERNAME + " = ?",
                selectionArg,
                null,
                null,
                null
        );
        while(cursor.moveToNext()) {
            result++;
        }

        if(result == 1){
            return  true;
        }
        return false;
    }

    public OauthTokenResponseDTO getTokenByUsername(String username){

        OauthTokenResponseDTO data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tables = new String[]{ACCESS_TOKEN,REFRESH_TOKEN,VALIDITY,TOKEN_TYPE,USERNAME};
        String[] selectionArg = new String[]{username};
        Cursor cursor = db.query(
                ACCESSTOKEN_TABLE,
                tables,
                USERNAME + " = ?",
                selectionArg,
                null,
                null,
                null
                );
        while(cursor.moveToNext()){
            try{
                data = new OauthTokenResponseDTO();
                for (String name: cursor.getColumnNames()) {
                    int index = cursor.getColumnIndex(name);
                    switch (name){
                        case ACCESS_TOKEN:
                            data.setAccess_token(cursor.getString(index));
                            break;
                        case REFRESH_TOKEN:
                            data.setRefresh_token(cursor.getString(index));
                            break;
                        case TOKEN_TYPE:
                            data.setToken_type(cursor.getString(index));
                            break;
                        case VALIDITY:
                            Date date = Calendar.getInstance().getTime();
                            Date tokenDate = new Date(cursor.getString(index));
                            System.out.println(date);
                            System.out.println(tokenDate);
                            long time = tokenDate.getTime() - date.getTime();
                            data.setValidity((int)time / 1000);
                            break;
                    }


                }
                Log.i("Database", "DAta found");
            }
            catch (Exception e){
                Log.i("Database", "Data NOT found");
            }
        }
        return  data;
    }

}
