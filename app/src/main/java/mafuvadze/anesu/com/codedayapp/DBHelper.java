package mafuvadze.anesu.com.codedayapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angellar Manguvo on 12/3/2015.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DB_NAME = "user.db";
    private static final String TABLE_NAME = "users";
    private static final String ID = "_id";
    private static final int ID_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int EMAIL_INDEX = 2;
    private static final int PASS_INDEX = 3;
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASS = "pass";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, "
            + NAME + " TEXT," + EMAIL + " TEXT," + PASS +  " TEXT);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    Context context;
    public DBHelper (Context context) {
        super(context, DB_NAME, null, VERSION);
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

    public boolean addUser(String email, String pass, String handle)
    {
        if(getAllUsers().contains(handle))
        {
            return false;
        }

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues content = new ContentValues();
            content.put(NAME, handle);
            content.put(EMAIL, email);
            content.put(PASS, pass);
            db.insert(TABLE_NAME, null, content);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public String getEmail()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, NAME, EMAIL, PASS}, ID+" = ?", new String[]{"1"}, null, null, null, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
            String email = cursor.getString(EMAIL_INDEX);
            return email;
        }
        else
        {
            return "no results";
        }
    }

    public String getPass()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, NAME, EMAIL, PASS}, ID + " = ?", new String[]{"1"}, null, null, null, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
            String email = cursor.getString(PASS_INDEX);
            return email;
        }
        else
        {
            return "no results";
        }
    }

    public List<String> getAllUsers()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> users = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    users.add(cursor.getString(NAME_INDEX));
                }
                while(cursor.moveToNext());

            }
        }
        else
        {
            return null;
        }

        return users;
    }

    public int getCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);
        cursor.close();;
        return cursor.getCount();
    }

    public boolean updateUserEmail(String newEmail)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues content = new ContentValues();
            content.put(EMAIL, newEmail);
            db.update(TABLE_NAME, content, ID + "=?", new String[]{"1"});
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean updateUserPass(String newPass)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues content = new ContentValues();
            content.put(PASS, newPass);
            db.update(TABLE_NAME, content, ID + "=?", new String[]{"1"});
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean deleteUser(String name)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            db.delete(TABLE_NAME, NAME + "=?", new String[]{name});
            db.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
