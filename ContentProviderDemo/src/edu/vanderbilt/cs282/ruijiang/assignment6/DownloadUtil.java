package edu.vanderbilt.cs282.ruijiang.assignment6;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.content.ContentResolver;

public class DownloadUtil{
    private final static int BUFFERSIZE = 1024;
    private final static int MAXFILEINDEX = 100000000;
    
    //only one static function since no object of this class needs to be instantiated
    public static String downloadImage(Context context, String url) {
        try {
            //store the content of the image to an InputStream
            InputStream in_stream = (InputStream) new URL(url).getContent();
            int nRead;
            byte[] data = new byte[BUFFERSIZE];
            Random rand = new Random();
            int file_index = rand.nextInt(MAXFILEINDEX);
            String file_name = Integer.toString(file_index) + ".jpg";
            //write from the InputStream to a file
            FileOutputStream out_stream = context.openFileOutput(file_name , Context.MODE_WORLD_READABLE);
            while ((nRead = in_stream.read(data, 0, data.length)) != -1) {
                out_stream.write(data, 0, nRead);
            }
            out_stream.close();
            //use a ContentValues to wrap the data for storing in the database
            ContentValues content_values = new ContentValues();
            content_values.put(FileDatabase.COL_URI, file_name);
            content_values.put(FileDatabase.COL_TIMESTAMP, System.currentTimeMillis());
            ContentResolver content_resolver = context.getContentResolver();
            Uri uri = content_resolver.insert(DownloadContentProvider.CONTENT_URI, content_values);
            Log.d("DOWNLOAD", "Download succeeded");
            return uri.toString();
        } catch (Exception e) {
            Log.d("DOWNLOAD", "Download failed", e);
            return null;
        }
    }
}
