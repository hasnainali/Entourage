package duxeye.com.entourage.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import duxeye.com.entourage.model.ResultFromDBSearchSchool;

/**
 * Created by Ondoor (Hasnain) on 8/2/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "yearbook";
    private static final String COLUMN_YEAR_BOOK_ID = "yearbook_id";
    private static final String COLUMN_YEAR_BOOK_NAME = "org_name";

    private static final String DATABASE_NAME = "yearbooks.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_YEAR_BOOK_ID + " integer primary key, "
            + COLUMN_YEAR_BOOK_NAME + " text not null);";

    public MySQLiteHelper(Context context,int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(MySQLiteHelper.class.getName(),"Upgrading database from version "
                + oldVersion + " to "+ newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean saveYearBook(String id,String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_YEAR_BOOK_ID, id);
        contentValues.put(COLUMN_YEAR_BOOK_NAME, name);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public  ArrayList<ResultFromDBSearchSchool> getAllYearBook() {
        ArrayList<ResultFromDBSearchSchool> mArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String id = String.valueOf(cursor.getLong(0));
            String name = cursor.getString(1);
            mArrayList.add(new ResultFromDBSearchSchool(id,name));
            cursor.moveToNext();
        }
        return mArrayList;
    }

}