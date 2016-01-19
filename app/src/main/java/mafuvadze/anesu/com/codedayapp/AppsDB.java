package com.score.senzservices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Anesu on 1/9/2016.
 */
public class ApplicationsDB extends SQLiteOpenHelper {
    Context context;
    private static String DB_NAME = "apps.db";
    private static String ID = "_id";
    private static String APP_NAME = "name";
    private static String APP_RATING = "rating";
    private static String APP_DESCRIPTION = "descr";
    private static String APP_INSTALLED = "isInstalled";
    private static String TABLE_NAME = "apps_table";
    private static int version = 1;
    private static int ID_INDEX = 0;
    private static int NAME_INDEX = 1;
    private static int RATING_INDEX = 2;
    private static int DESCRPTION_INDEX = 3;
    private static int INSTALLED_INDEX = 3;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, "
            + APP_NAME + " TEXT," + APP_RATING + " TEXT," + APP_DESCRIPTION +  " TEXT," + APP_INSTALLED + " INTEGER DEFAULT 0);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    public ApplicationsDB(Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addApp(String name, String rating, String descr, boolean isInstalled)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();
        content.put(APP_NAME, name);
        content.put(APP_RATING, rating);
        content.put(APP_DESCRIPTION, descr);
        content.put(APP_INSTALLED, isInstalled == true ? 1:0);
        db.insert(TABLE_NAME, null, content);
    }

    public ArrayList<ApplicationInfo> getAllApps()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ApplicationInfo> apps = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    String name = cursor.getString(NAME_INDEX);
                    float rating = Float.parseFloat(cursor.getString(RATING_INDEX));
                    String descr = cursor.getString(DESCRPTION_INDEX);
                    Boolean isInstalled = cursor.getInt(INSTALLED_INDEX) == 1 ? true : false;
                    apps.add(new ApplicationInfo(context, name, isInstalled, rating, descr, null));
                }
                while(cursor.moveToNext());

            }
        }

        return apps;
    }
}
