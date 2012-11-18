package edu.vanderbilt.cs282.ruijiang.assignment6;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class DisplayCursorAdapter extends CursorAdapter {

    public DisplayCursorAdapter(Context context, Cursor c) {
        super(context, c);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        try {
            //create an InputStream and load the file pointed to by the uri in the cursor into the stream
            InputStream in_stream;
            in_stream = (InputStream) context.openFileInput(cursor.getString(cursor.getColumnIndex(FileDatabase.COL_URI)));
            //decode the file into bitmap and set it to the ImageView
            Bitmap bitmap = BitmapFactory.decodeStream(in_stream);
            ((ImageView) view).setImageBitmap(bitmap);
            Log.d(getClass().getSimpleName(), "Opening file: "+cursor.getString(cursor.getColumnIndex(FileDatabase.COL_URI)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.d(getClass().getSimpleName(), "Failed to display image", e);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //create a new ImageView and call bindView on it; the rest is taken care of by the adapter
        ImageView new_image = new ImageView(context);
        bindView(new_image, context, cursor);
        return new_image;
    }

}
