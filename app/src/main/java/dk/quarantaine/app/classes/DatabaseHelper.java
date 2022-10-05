package dk.quarantaine.app.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import dk.quarantaine.commons.dto.OauthTokenResponseDTO;
import dk.quarantaine.app.datamodel.LocationModel;

import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOCATION_TABLE = "LOCATION_TABLE";
    private static final String LOCATION_COLUMN_LAT = "LAT";
    private static final String LOCATION_COLUMN_LON = "LON";
    private static final String LOCATION_COLUMN_TIME = "TIME";
    private static final String LOCATION_COLUMN_ID = "ID";

    private static final String ACCESSTOKEN_TABLE = "ACCESSTOKEN";
    private static final String ACCESSTOKEN_COLUMN_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ACCESSTOKEN_COLUMN_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String ACCESSTOKEN_COLUMN_VALIDITY = "VALIDITY";
    private static final String ACCESSTOKEN_COLUMN_TOKEN_TYPE = "TOKEN_TYPE";
    private static final String ACCESSTOKEN_COLUMN_USERNAME = "USERNAME";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "quarantaine.db", null, 1);
    }

    // This is going to be called the first a database is accessed, should be code to generate new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + LOCATION_TABLE + " (" + LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOCATION_COLUMN_LAT + " DOUBLE, " + LOCATION_COLUMN_LON + " DOUBLE, " + LOCATION_COLUMN_TIME + " DATE)";

        db.execSQL(createTableStatement);

        String createTableStatementAccesstoken = "CREATE TABLE " + ACCESSTOKEN_TABLE + " (" + ACCESSTOKEN_COLUMN_USERNAME + " TEXT PRIMARY KEY NOT NULL, " + ACCESSTOKEN_COLUMN_ACCESS_TOKEN + " TEXT, " + ACCESSTOKEN_COLUMN_REFRESH_TOKEN + " TEXT, "+ ACCESSTOKEN_COLUMN_TOKEN_TYPE +" TEXT, "+ ACCESSTOKEN_COLUMN_VALIDITY + " DATE)";

        db.execSQL(createTableStatementAccesstoken);
    }

    /**
     * Gets the currently logged in user
     * @return the name of the user.
     */
    public String getLoggedInUser(){

        String username = null;
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tables = new String[]{ACCESSTOKEN_COLUMN_USERNAME};
        Cursor cursor = db.rawQuery("SELECT USERNAME FROM "+ ACCESSTOKEN_TABLE + " LIMIT 1;",null);

        while(cursor.moveToNext()) {
            username = cursor.getString(0);
        }
        return username;
    }

    /**
     * Deletes the table containing the user(s)
     * @return if data was deleted.
     */
    public boolean deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(ACCESSTOKEN_TABLE, null, null);
        if(delete > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // Called everytime the database version updates
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Insert location data into local datbase
     * @param locationModel data to insert
     * @return if data was inserted
     */
    public boolean addLocation(LocationModel locationModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION_COLUMN_LAT, locationModel.getLat());
        values.put(LOCATION_COLUMN_LON, locationModel.getLon());
        values.put(LOCATION_COLUMN_TIME, locationModel.getTime().toString());

        long insert = db.insert(LOCATION_TABLE, null, values);

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Delete the location data from the local database
     * @return if data was saved
     */
    public boolean deleteLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(LOCATION_TABLE, null, null);
        if(delete > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Inserts or updates access token data in database
     * @param username the username to update data for
     * @param tokenResponseDTO the new data to save
     * @return if data was updated.
     */
    public boolean insertOrUpdateAccessToken(String username, OauthTokenResponseDTO tokenResponseDTO){
        deleteUsers();
        if(doesUserEntryExist(username)){
            return updateDataAccessToken(username,tokenResponseDTO);
        }
        else {
            return insertDataAccessToken(username,tokenResponseDTO);
        }
    }

    /**
     * Inserts data on access token in database
     * @param username username to update for
     * @param tokenResponseDTO the access data to save
     * @return if data was saved
     */
    public boolean insertDataAccessToken (String username, OauthTokenResponseDTO tokenResponseDTO) {
        deleteUsers();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCESSTOKEN_COLUMN_ACCESS_TOKEN, tokenResponseDTO.getAccess_token());
        values.put(ACCESSTOKEN_COLUMN_REFRESH_TOKEN, tokenResponseDTO.getRefresh_token());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,tokenResponseDTO.getValidity());
        Date time = cal.getTime();
        values.put(ACCESSTOKEN_COLUMN_VALIDITY, time.toString());
        values.put(ACCESSTOKEN_COLUMN_TOKEN_TYPE, tokenResponseDTO.getToken_type());
        values.put(ACCESSTOKEN_COLUMN_USERNAME, username);

        long insert = db.insert(ACCESSTOKEN_TABLE, null, values);

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Updates existing data on access token
     * @param username username to update on
     * @param tokenResponseDTO the data to update with
     * @return if data was updated
     */
    public boolean updateDataAccessToken(String username, OauthTokenResponseDTO tokenResponseDTO) {
        deleteUsers();
        try {

            SQLiteDatabase db = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(ACCESSTOKEN_COLUMN_ACCESS_TOKEN, tokenResponseDTO.getAccess_token());
            values.put(ACCESSTOKEN_COLUMN_REFRESH_TOKEN, tokenResponseDTO.getRefresh_token());

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND,tokenResponseDTO.getValidity());
            Date time = cal.getTime();

            values.put(ACCESSTOKEN_COLUMN_VALIDITY, time.toString());
            values.put(ACCESSTOKEN_COLUMN_TOKEN_TYPE, tokenResponseDTO.getToken_type());
            String[] usernamevalue = new String[]{username};

            long insert = db.update(ACCESSTOKEN_TABLE, values,  ACCESSTOKEN_COLUMN_USERNAME + " = ?", usernamevalue );

            if(insert == -1){
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return  false;
        }

    }

    /**
     * Gets if the user entry exists
     * @param username the username to check on
     * @return if the user exists
     */
    private boolean doesUserEntryExist(String username){
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tables = new String[]{ACCESSTOKEN_COLUMN_USERNAME};
        String[] selectionArg = new String[]{username};
        Cursor cursor = db.query(
                ACCESSTOKEN_TABLE,
                tables,
                ACCESSTOKEN_COLUMN_USERNAME + " = ?",
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

    /**
     * Gets the access token based on username
     * @param username the username to get information
     * @return the access token data.
     */
    public OauthTokenResponseDTO getAccessTokenByUsername(String username){

        OauthTokenResponseDTO data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tables = new String[]{ACCESSTOKEN_COLUMN_ACCESS_TOKEN,ACCESSTOKEN_COLUMN_REFRESH_TOKEN,ACCESSTOKEN_COLUMN_VALIDITY,ACCESSTOKEN_COLUMN_TOKEN_TYPE,ACCESSTOKEN_COLUMN_USERNAME};
        String[] selectionArg = new String[]{username};
        Cursor cursor = db.query(
                ACCESSTOKEN_TABLE,
                tables,
                ACCESSTOKEN_COLUMN_USERNAME + " = ?",
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
                        case ACCESSTOKEN_COLUMN_ACCESS_TOKEN:
                            data.setAccess_token(cursor.getString(index));
                            break;
                        case ACCESSTOKEN_COLUMN_REFRESH_TOKEN:
                            data.setRefresh_token(cursor.getString(index));
                            break;
                        case ACCESSTOKEN_COLUMN_TOKEN_TYPE:
                            data.setToken_type(cursor.getString(index));
                            break;
                        case ACCESSTOKEN_COLUMN_VALIDITY:
                            Date date = Calendar.getInstance().getTime();
                            Date tokenDate = new Date(cursor.getString(index));
                            System.out.println(date);
                            System.out.println(tokenDate);
                            long time = tokenDate.getTime() - date.getTime();
                            data.setValidity((int)time / 1000);
                            break;
                    }


                }
                Log.i("Database", "Data found");
            }
            catch (Exception e){
                Log.i("Database", "Data NOT found");
            }
        }
        return  data;
    }
}
