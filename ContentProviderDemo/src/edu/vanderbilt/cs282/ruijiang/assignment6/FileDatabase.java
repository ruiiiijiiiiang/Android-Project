package edu.vanderbilt.cs282.ruijiang.assignment6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FileDatabase extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "file_data";
    
    public static final String TABLE_NAME = "downloaded_files";
    public static final String ID = "_id";
    public static final String COL_URI = "uri";
    public static final String COL_TIMESTAMP = "timestamp";
    
    private static final String CREATE_TABLE_QUERY = "create table " +
            TABLE_NAME + " (" + ID + " integer primary key autoincrement, " +
    COL_URI + " text not null, " + COL_TIMESTAMP + " long not null);";
    
    private static final String DB_SCHEMA = CREATE_TABLE_QUERY;
    
    public FileDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DB_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
