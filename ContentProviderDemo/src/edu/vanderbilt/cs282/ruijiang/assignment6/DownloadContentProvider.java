package edu.vanderbilt.cs282.ruijiang.assignment6;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DownloadContentProvider extends ContentProvider {
    //some functions are not used here thus not implemented
    
    private static final String AUTHORITY = 
            "edu.vanderbilt.cs282.ruijiang.assignment6.DownloadContentProvider";
    private static final String MESSAGES_BASE_PATH = "files";
    //these two strings need to be public because they need to be accessed from outside
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + MESSAGES_BASE_PATH);
    public static final String CONTENT_TYPE = 
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/files";
    private FileDatabase fileDB;
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        //insert the data in the ContentValues into the database and return the uri
        SQLiteDatabase db = fileDB.getWritableDatabase();
        long rowId = db.insert(FileDatabase.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        fileDB = new FileDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        //create a cursor that points to the queried content and return it
        Cursor cursor = fileDB.getReadableDatabase().query(FileDatabase.TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
